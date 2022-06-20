package com.greazi.plugin.settings;

import com.greazi.plugin.PluginCore;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Example configuration suited for read-only, very useful for
 * storing the main settings file which you do not need to update
 * at runtime and need to access from anywhere.
 */
public final class SimpleSettings {

	/**
	 * All the static settings methods
	 */
	public static List<String> DEBUG_SECTION;
	public static String PREFIX;
	public static int WILD_BORDER;
	public static String MINIMESSAGE_TEST;

	/**
	 * (Re)load configuration using the default settings.yml file
	 */
	public static void load() {
		final PluginCore instance = PluginCore.getInstance();
		final String path = "settings.yml";
		final File settingsFile = new File(PluginCore.getData(), path);

		// Use Bukkit to copy settings.yml over from your JAR to the plugin's folder
		if (!settingsFile.exists())
			instance.saveResource(path, false);

		// Load default settings for (new) keys that are not on your disk
		final YamlConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource(path)));
		final YamlConfiguration config = YamlConfiguration.loadConfiguration(settingsFile);

		// Assign to use defaults for keys not on disk but inside jar
		config.addDefaults(defaults);

		DEBUG_SECTION = config.getStringList("Debug");
		PREFIX = config.getString("Prefix");
		WILD_BORDER = config.getInt("WildBorder");
		MINIMESSAGE_TEST = config.getString("MiniMessageTest");

		printFields(SimpleSettings.class);
	}

	/**
	 * Get the
	 *
	 * @param clazz
	 */
	@SneakyThrows
	private static void printFields(final Class<?> clazz) {
		for (final Field field : clazz.getDeclaredFields())
			System.out.println(clazz.getSimpleName() + "." + field.getName() + " ---> " + field.get(null));

		// Also print fields for inner classes (such as Boss here)
		for (final Class<?> superClass : clazz.getDeclaredClasses())
			printFields(superClass);
	}
}
