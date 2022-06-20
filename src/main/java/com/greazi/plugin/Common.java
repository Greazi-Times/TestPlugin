package com.greazi.plugin;

import com.greazi.plugin.debug.FoException;
import com.greazi.plugin.settings.SimpleSettings;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	private static final MiniMessage miniMessage = MiniMessage.miniMessage();

	private static final String logPrefix = SimpleSettings.PREFIX;

	private static final String HEX_WEBCOLOR_PATTERN
			= "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";

	private static final Pattern pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);

	public static boolean isValid(final String colorCode) {
		final Matcher matcher = pattern.matcher(colorCode);
		return matcher.matches();
	}

	/**
	 * A simple method that colorizes text using the MiniMessage API
	 *
	 * @param message The message to colorize
	 * @return The colorized text
	 */
	public static Component colorize(String message) {
		// Replace all color codes with the original color codes
		message = ChatColor.translateAlternateColorCodes('&', message);

		// A very very very ugly way of doing this, but it works
		message = message.replace("§4", "<dark_red>");
		message = message.replace("§c", "<red>");
		message = message.replace("§6", "<gold>");
		message = message.replace("§e", "<yellow>");
		message = message.replace("§2", "<dark_green>");
		message = message.replace("§a", "<green>");
		message = message.replace("§b", "<aqua>");
		message = message.replace("§3", "<dark_aqua>");
		message = message.replace("§1", "<dark_blue>");
		message = message.replace("§9", "<blue>");
		message = message.replace("§d", "<light_purple>");
		message = message.replace("§5", "<dark_purple>");
		message = message.replace("§f", "<white>");
		message = message.replace("§7", "<gray>");
		message = message.replace("§8", "<dark_gray>");
		message = message.replace("§0", "<black>");
		message = message.replace("§r", "<reset>");
		message = message.replace("§l", "<bold>");
		message = message.replace("§o", "<italic>");
		message = message.replace("§n", "<underlined>");
		message = message.replace("§m", "<strikethrough>");
		message = message.replace("§k", "<obfuscated>");

		// Replace \n with the <newline> tag of MiniMessage
		message = message.replace("\n", "<newline>");

		// Colorize the text with the MiniMessage API
		return miniMessage.deserialize(message);
	}

	/**
	 * A simple method that colorizes text using the MiniMessage API
	 *
	 * @param message The message to colorize
	 * @return The colorized text
	 */
	public static String colorizeLegacy(final String message) {
		final String test = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));
		return ChatColor.translateAlternateColorCodes('&', test);
	}

	/**
	 * A simple method that colorizes text using the MiniMessage API
	 *
	 * @param messages The messages to colorize
	 * @return The colorized text
	 */
	public static List<String> colorizeLegacy(final String... messages) {
		final List<String> coloredMessages = new ArrayList<>();
		for (String message : messages) {
			message = colorizeLegacy(message);
			coloredMessages.add(message);
		}
		return coloredMessages;
	}

	/**
	 * A simple method that colorizes text using the MiniMessage API
	 *
	 * @param messages The messages to colorize
	 * @return The colorized text
	 */
	public static List<String> colorizeLegacy(final List<String> messages) {
		final List<String> coloredMessages = new ArrayList<>();
		for (String message : messages) {
			message = colorizeLegacy(message);
			coloredMessages.add(message);
		}
		return coloredMessages;
	}

	/**
	 * A simple method that colorizes text and sends it to the player
	 *
	 * @param player  The player to send the message to
	 * @param message The message to send
	 */
	public static void tell(final Player player, final String message) {
		final Audience audience = (Audience) player;

		audience.sendMessage(colorize(message));
	}

	/**
	 * A simple method that tells all players in the server a message
	 *
	 * @param message The message to send
	 */
	public static void tellAll(String message) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			final Audience audience = (Audience) player;

			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			final LocalDateTime now = LocalDateTime.now();

			if (message.contains("{time}"))
				message = message.replace("{time}", dtf.format(now) + "");

			audience.sendMessage(colorize(message));
		}
	}

	/**
	 * A simple log method that allows you to log them to console
	 *
	 * @param message The message to log
	 */
	public static void log(final String... message) {
		log(true, message);
	}

	/**
	 * A simple method to log a single message to console
	 *
	 * @param message The message to log
	 */
	public static void log(final String message) {
		log(true, message);
	}

	/**
	 * A simple method to log a message as a warning in console
	 *
	 * @param message The message to log
	 */
	public static void warn(final String message) {
		log(false, "&6[WARNING] &f" + message);
	}

	/**
	 * A simple method to log a message as an error in console
	 *
	 * @param message The message to log
	 */
	public static void error(final String message) {
		log(false, "&c[ERROR] " + message);
	}

	/**
	 * A simple method to log a message without a prefix in console
	 *
	 * @param message The message to log
	 */
	public static void logNoPrefix(final String message) {
		log(false, message);
	}

	/**
	 * A simple method to log a message to console
	 *
	 * @param message The message to log
	 */
	public static void logNoPrefix(final String... message) {
		log(false, message);
	}

	/**
	 * A simple method to log a message to console
	 *
	 * @param prefix   Whether to prefix the message with the log prefix
	 * @param messages The messages to log
	 */
	public static void log(final boolean prefix, final String... messages) {
		// Check if the message doesn't contain any nulls
		if (messages == null)
			return;

		// Get the console sender
		final CommandSender console = Bukkit.getConsoleSender();

		// Check if the message doesn't contain any nulls
		if (console == null)
			throw new FoException("Failed to initialize Console Sender, are you running GreaziCore under a Bukkit/Spigot server?");

		// Check if the message should have a prefix
		if (prefix) {
			// Loop through all the messages
			for (final String msg : messages) {
				// Send the message to the console with the prefix
				console.sendMessage(ChatColor.translateAlternateColorCodes('&', logPrefix + " " + msg));
			}
		} else {
			// Loop through all the messages
			for (final String msg : messages) {
				// Send the message to the console
				console.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// Aesthetics
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Returns a long ------ console line
	 *
	 * @return
	 */
	public static String consoleLine() {
		return "!-----------------------------------------------------!";
	}

	/**
	 * Returns a long ______ console line
	 *
	 * @return
	 */
	public static String consoleLineSmooth() {
		return "______________________________________________________________";
	}

	/**
	 * Returns a long -------- chat line
	 *
	 * @return
	 */
	public static String chatLine() {
		return "*---------------------------------------------------*";
	}

	/**
	 * Returns a long &m----------- chat line with strike effect
	 *
	 * @return
	 */
	public static String chatLineSmooth() {
		return "&m-----------------------------------------------------";
	}
}
