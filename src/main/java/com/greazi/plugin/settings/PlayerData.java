package com.greazi.plugin.settings;

import com.greazi.plugin.PluginCore;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A second example of storing/loading settings using a custom file for each player.
 */
@Getter
public final class PlayerData {

	private static Map<UUID, PlayerData> playerData = new HashMap<>();

	private final String playerName;
	private final UUID uuid;
	private final YamlConfiguration config;
	private final File file;

	private double health;
	private String tabListName;

	private PlayerData(final String playerName, final UUID uuid) {
		this.playerName = playerName;
		this.uuid = uuid;
		this.file = this.loadFile();
		this.config = new YamlConfiguration();

		this.load();
		this.save();
	}

	private File loadFile() {
		final PluginCore instance = PluginCore.getInstance();
		final String path = "players/" + this.uuid + ".yml";

		final File file = new File(instance.getDataFolder(), path);

		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();

		if (!file.exists())
			try {
				file.createNewFile();

			} catch (final IOException ex) {
				throw new RuntimeException(ex);
			}

		return file;
	}

	private void load() {

		try {
			this.config.load(this.file);

		} catch (final Throwable t) {
			t.printStackTrace();

			return;
		}

		this.health = this.config.getDouble("Health", 20);
		this.tabListName = this.config.getString("Tablist_Name", this.playerName);
	}

	private void save() {

		this.config.options().header("My\nHeader");
		this.config.options().copyHeader(true);

		this.config.set("Health", this.health);
		this.config.set("Tablist_Name", this.tabListName);

		try {

			// This removes comments inside the file, but will keep any custom values you stored there
			this.config.save(this.file);

		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}

	public void setHealth(final double health) {
		this.health = health;

		this.save();
	}

	public void setTabListName(final String tabListName) {
		this.tabListName = tabListName;

		this.save();
	}

	public static PlayerData from(final Player player) {
		final UUID uuid = player.getUniqueId();
		PlayerData data = playerData.get(uuid);

		if (data == null) {
			data = new PlayerData(player.getName(), uuid);

			playerData.put(uuid, data);
		}

		return data;
	}

	public static void remove(final Player player) {
		playerData.remove(player.getUniqueId());
	}
}
