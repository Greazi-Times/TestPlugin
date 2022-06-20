package com.greazi.plugin.settings;

import com.greazi.plugin.PluginCore;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class Lang {

	public static String NO_PERMISSION;

	public static class Command {
		public static String NO_CONSOLE;
		public static String RELOADING;
		public static String CONSOLE_MISSING_PLAYER;
	}

	public static class Wild {

		public static class Self {
			public static String SUCCESS;
			public static String FAIL;
		}

		public static class Other {

			public static class Success {
				public static String SELF;
				public static String TARGET;
			}

			public static String FAIL;
		}
	}

	public static class NameTag {
		public static String ENABLED;
		public static String DISABLED;

		public static class Sub {
			public static String ENABLED;
			public static String DISABLED;
		}
	}

	public static class Hologram {
		public static String SUCCESS;
		public static String FAILED;
	}

	/**
	 * (Re)load configuration using the default settings.yml file
	 */
	public static void load() {
		final PluginCore instance = PluginCore.getInstance();
		final String path = "localization/lang.yml";
		final File settingsFile = new File(PluginCore.getData(), path);

		// Use Bukkit to copy settings.yml over from your JAR to the plugin's folder
		if (!settingsFile.exists())
			instance.saveResource(path, false);

		// Load default settings for (new) keys that are not on your disk
		final YamlConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(instance.getResource(path)));
		final YamlConfiguration config = YamlConfiguration.loadConfiguration(settingsFile);

		// Assign to use defaults for keys not on disk but inside jar
		config.addDefaults(defaults);

		NO_PERMISSION = config.getString("NoPermission");

		Command.NO_CONSOLE = config.getString("Command.NoConsole");
		Command.CONSOLE_MISSING_PLAYER = config.getString("Command.ConsoleMissingPlayer");
		Command.RELOADING = config.getString("Command.Reloading");

		Wild.Self.SUCCESS = config.getString("Wild.Self.Success");
		Wild.Self.FAIL = config.getString("Wild.Self.Fail");
		Wild.Other.Success.SELF = config.getString("Wild.Other.Success.Self");
		Wild.Other.Success.TARGET = config.getString("Wild.Other.Success.Target");
		Wild.Other.FAIL = config.getString("Wild.Other.Fail");

		NameTag.ENABLED = config.getString("NameTag.Enabled");
		NameTag.DISABLED = config.getString("NameTag.Disabled");
		NameTag.Sub.ENABLED = config.getString("NameTag.Sub.Enabled");
		NameTag.Sub.DISABLED = config.getString("NameTag.Sub.Disabled");

		Hologram.SUCCESS = config.getString("Hologram.Success");
		Hologram.FAILED = config.getString("Hologram.Failed");


		printFields(SimpleSettings.class);
	}

	// Lazy: You can use reflection to get values of all fields -- even load them, although
	// this is not recommended since field names can be easily changed (i.e. by obfuscation),
	// leading to silent problems
	@SneakyThrows
	private static void printFields(final Class<?> clazz) {
		for (final Field field : clazz.getDeclaredFields())
			System.out.println(clazz.getSimpleName() + "." + field.getName() + " ---> " + field.get(null));

		// Also print fields for inner classes (such as Boss here)
		for (final Class<?> superClass : clazz.getDeclaredClasses())
			printFields(superClass);
	}
}
