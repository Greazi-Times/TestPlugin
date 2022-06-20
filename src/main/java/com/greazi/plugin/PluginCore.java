package com.greazi.plugin;

import com.greazi.plugin.command.*;
import com.greazi.plugin.listener.PlayerListener;
import com.greazi.plugin.models.HealthTag;
import com.greazi.plugin.settings.Lang;
import com.greazi.plugin.settings.SimpleSettings;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

// TODO: (Global) Clean up the code
// TODO: (Lang) Make the lang file better

public final class PluginCore extends JavaPlugin {

	/**
	 * The instance of this plugin
	 */
	private static volatile PluginCore instance;

	/**
	 * Shortcut for getDescription().getVersion()
	 */
	@Getter
	private static String version;

	/**
	 * Shortcut for getName()
	 */
	@Getter
	private static String named;

	/**
	 * Shortcut for getFile()
	 */
	@Getter
	private static File source;

	/**
	 * Shortcut for getDataFolder()
	 */
	@Getter
	private static File data;

	/**
	 * Get the Vault API chat manager
	 */
	public Chat chat;

	/**
	 * Get the enabled state of the name tags
	 */
	public boolean nameTagEnabled = true;

	/**
	 * Get the enabled state of the health tags
	 */
	public boolean heartTagEnabled = true;

	/**
	 * Returns the instance of {@link PluginCore}.
	 * <p>
	 * It is recommended to override this in your own {@link PluginCore}
	 * implementation so you will get the instance of that, directly.
	 *
	 * @return this instance
	 */
	public static PluginCore getInstance() {
		if (instance == null) {
			try {
				instance = JavaPlugin.getPlugin(PluginCore.class);

			} catch (final IllegalStateException ex) {
				if (Bukkit.getPluginManager().getPlugin("PlugMan") != null)
					Bukkit.getLogger().severe("Failed to get instance of the plugin, if you reloaded using PlugMan you need to do a clean restart instead.");

				throw ex;
			}

			Objects.requireNonNull(instance, "Cannot get a new instance! Have you reloaded?");
		}

		return instance;
	}

	/**
	 * Get if the instance that is used across the library has been set. Normally it
	 * is always set, except for testing.
	 *
	 * @return if the instance has been set.
	 */
	public static boolean hasInstance() {
		return instance != null;
	}

	@Override
	public void onLoad() {

		// Set the instance
		getInstance();

		// Cache results for best performance
		version = instance.getDescription().getVersion();
		named = instance.getDataFolder().getName();
		source = instance.getFile();
		data = instance.getDataFolder();

		SimpleSettings.load();
		Lang.load();

		Common.log("Loading " + named + " v" + version);

		final String version = Bukkit.getVersion();

		if (!version.contains("Paper")
				&& !version.contains("Purpur")
				&& !version.contains("-Spigot")) {
			Bukkit.getLogger().severe(Common.consoleLine());
			Bukkit.getLogger().warning("Warning about " + named + ": You're not using Paper!");
			Bukkit.getLogger().warning("Detected: " + version);
			Bukkit.getLogger().warning("");
			Bukkit.getLogger().warning("Third party forks are known to alter server in unwanted");
			Bukkit.getLogger().warning("ways. If you have issues with " + named + " use Paper");
			Bukkit.getLogger().warning("from PaperMC.io otherwise you may not receive our support.");
			Bukkit.getLogger().severe(Common.consoleLine());
		}
	}

	/**
	 * The part that runs when the plugin is enabling
	 */
	@Override
	public void onEnable() {
		// Plugin startup logic
		Common.log("Enabling " + named + " v" + version);

		// Run the setup parts
		setupVault();
		loadCommands();
		loadEvents();

		// Register the health tag
		HealthTag.register();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		Common.log("Disabling " + named + " v" + version);


		// Close custom inventories because bukkit removes temporary metadata from players on /reload
		// to prevent menus from being broken when you reload the server while players have their menus opened
		for (final Player player : Bukkit.getOnlinePlayers()) {

			// Call onClose() in your menus manually, apparently bukkit does not call InventoryCloseEvent here (you may
			// need to check this with your MC version you are using)
			if (player.hasMetadata("CustomInventory_" + PluginCore.getNamed())) {

				// Just in case to make sure all menu MetaData is removed
				player.removeMetadata("CustomInventory_" + PluginCore.getNamed(), PluginCore.getInstance());
			}

			// Close all the Menus that are open
			player.closeInventory();
		}
	}

	/**
	 * Set up the Vault API
	 */
	private void setupVault() {
		// Register the chat manager
		final RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		// Set the chat manager
		chat = rsp.getProvider();
	}

	/**
	 * Load the commands
	 */
	private void loadCommands() {
		// Load all the commands
		getCommand("spawnentity").setExecutor(new SpawnEntityCommand());
		getCommand("greazicore").setExecutor(new GreaziCoreCommand());
		getCommand("hologram").setExecutor(new HologramCommand());
		getCommand("wild").setExecutor(new WildCommand());
		getCommand("sandbox").setExecutor(new SandboxCommand());
	}

	/**
	 * Load the events
	 */
	public void loadEvents() {
		getServer().getPluginManager().registerEvents(PlayerListener.getInstance(), this);
	}
}
