package com.greazi.plugin.debug;

import com.greazi.plugin.Common;
import com.greazi.plugin.PluginCore;
import com.greazi.plugin.settings.SimpleSettings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for solving problems and errors
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Debugger {

	/**
	 * Stores messages to be printed out at once at the end,
	 * the key is the debug section and the list contains messages that will be connected
	 * and printed.
	 */
	private static final Map<String, ArrayList<String>> pendingMessages = new HashMap<>();

	/**
	 * The debug mode is automatically detected when the debug.lock file is present in the plugin folder
	 */
	@Getter
	private static boolean debugModeEnabled = false;

	/**
	 * Loads debug mode, called automatically in {@link PluginCore}
	 */
	public static void detectDebugMode() {
		if (new File(PluginCore.getData(), "debug.lock").exists()) {
			debugModeEnabled = true;

			Bukkit.getLogger().info("Detected debug.lock file, debug features enabled!");

		} else
			debugModeEnabled = false;
	}

	/**
	 * Prints a debug messages to the console if the given section is being debugged
	 * <p>
	 * You can set if the section is debugged by setting it in "Debug" key in your settings.yml,
	 * by default your class extending {@link SimpleSettings}
	 *
	 * @param section
	 * @param messages
	 */
	public static void debug(final String section, final String... messages) {
		if (isDebugged(section))
			for (final String message : messages)
				if (PluginCore.hasInstance())
					Common.log("[" + section + "] " + message);
				else
					System.out.println("[" + section + "] " + message);
	}

	/**
	 * Puts a message for the specific section into the queue. These are stored there until
	 * you call {@link #push(String)} and then put together and printed.
	 *
	 * @param section
	 * @param message
	 */
	public static void put(final String section, final String message) {
		if (!isDebugged(section))
			return;

		final ArrayList<String> list = pendingMessages.getOrDefault(section, new ArrayList<>());
		list.add(message);

		pendingMessages.put(section, list);
	}

	/**
	 * Puts the message at the end of the pending message queue and pushes the final log
	 * to the console
	 *
	 * @param section
	 * @param message
	 */
	public static void push(final String section, final String message) {
		put(section, message);
		push(section);
	}

	/**
	 * Clears all pending messages from {@link #put(String, String)}, puts them together
	 * and prints them into your console
	 *
	 * @param section
	 */
	public static void push(final String section) {
		if (!isDebugged(section))
			return;

		final List<String> parts = pendingMessages.remove(section);

		if (parts == null)
			return;

		final String whole = String.join("", parts);

		for (final String message : whole.split("\n"))
			debug(section, message);
	}

	/**
	 * Get if the given section is being debugged
	 * <p>
	 * You can set if the section is debugged by setting it in "Debug" key in your settings.yml,
	 * by default your class extending {@link SimpleSettings}
	 * <p>
	 * If you set Debug to ["*"] this will always return true
	 *
	 * @param section
	 * @return
	 */
	public static boolean isDebugged(final String section) {
		return SimpleSettings.DEBUG_SECTION.contains(section) || SimpleSettings.DEBUG_SECTION.contains("*");
	}


	// ----------------------------------------------------------------------------------------------------
	// Utility methods
	// ----------------------------------------------------------------------------------------------------

	/**
	 * Print out where your method is being called from
	 * Such as: YourClass > YourMainClass > MinecraftServer > Thread
	 * <p>
	 * Also can print line numbers YourClass#LineNumber
	 *
	 * @param trackLineNumbers
	 * @return
	 */
	public static List<String> traceRoute(final boolean trackLineNumbers) {
		final Exception exception = new RuntimeException("I love horses");
		final List<String> paths = new ArrayList<>();

		for (final StackTraceElement el : exception.getStackTrace()) {
			final String[] classNames = el.getClassName().split("\\.");
			final String className = classNames[classNames.length - 1];
			final String line = el.toString();

			if (line.contains("net.minecraft.server") || line.contains("org.bukkit.craftbukkit"))
				break;

			if (line.contains("org.bukkit.plugin.java.JavaPluginLoader") || line.contains("org.bukkit.plugin.SimplePluginManager") || line.contains("org.bukkit.plugin.JavaPlugin"))
				continue;

			if (!paths.contains(className))
				paths.add(className + "#" + el.getMethodName() + (trackLineNumbers ? "(" + el.getLineNumber() + ")" : ""));
		}

		// Remove call to self
		if (!paths.isEmpty())
			paths.remove(0);

		return paths;
	}

	/**
	 * Prints stack trace until we reach the native MC/Bukkit with a custom message
	 *
	 * @param message the message to wrap stack trace around
	 */
	public static void printStackTrace(final String message) {
		final StackTraceElement[] trace = new Exception().getStackTrace();

		print("!----------------------------------------------------------------------------------------------------------!");
		print(message);
		print("!----------------------------------------------------------------------------------------------------------!");

		for (int i = 1; i < trace.length; i++) {
			final String line = trace[i].toString();

			if (canPrint(line))
				print("\tat " + line);
		}

		print("--------------------------------------------------------------------------------------------------------end-");
	}

	/**
	 * Prints a Throwable's first line and stack traces.
	 * <p>
	 * Ignores the native Bukkit/Minecraft server.
	 *
	 * @param throwable the throwable to print
	 */
	public static void printStackTrace(@NonNull final Throwable throwable) {

		// Load all causes
		final List<Throwable> causes = new ArrayList<>();

		if (throwable.getCause() != null) {
			Throwable cause = throwable.getCause();

			do
				causes.add(cause);
			while ((cause = cause.getCause()) != null);
		}

		if (throwable instanceof FoException && !causes.isEmpty())
			// Do not print parent exception if we are only wrapping it, saves console spam
			print(throwable.getMessage());
		else {
			print(throwable.toString());

			printStackTraceElements(throwable);
		}

		if (!causes.isEmpty()) {
			final Throwable lastCause = causes.get(causes.size() - 1);

			print(lastCause.toString());
			printStackTraceElements(lastCause);
		}
	}

	private static void printStackTraceElements(final Throwable throwable) {
		for (final StackTraceElement element : throwable.getStackTrace()) {
			final String line = element.toString();

			if (canPrint(line))
				print("\tat " + line);
		}
	}

	/**
	 * Returns whether a line is suitable for printing as an error line - we ignore stuff from NMS and other spam as this is not needed
	 *
	 * @param message
	 * @return
	 */
	private static boolean canPrint(final String message) {
		return !message.contains("net.minecraft") &&
				!message.contains("org.bukkit.craftbukkit") &&
				!message.contains("org.github.paperspigot.ServerScheduler") &&
				!message.contains("nashorn") &&
				!message.contains("javax.script") &&
				!message.contains("org.yaml.snakeyaml") &&
				!message.contains("sun.reflect") &&
				!message.contains("sun.misc") &&
				!message.contains("java.lang.Thread.run") &&
				!message.contains("java.util.concurrent.ThreadPoolExecutor");
	}

	// Print a simple console message
	private static void print(final String message) {
		if (PluginCore.hasInstance())
			Common.logNoPrefix(message);
		else
			System.out.println(message);
	}
}
