package com.greazi.plugin.models;

import com.greazi.plugin.Common;
import com.greazi.plugin.PluginCore;
import com.greazi.plugin.util.CustomMaterial;
import com.greazi.plugin.util.Human;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Menus {

	public static void main(final Player player) {
		final @NotNull String title = Common.colorizeLegacy("&6&lGreazi Core Menu");
		final Inventory inventory = Bukkit.createInventory(player, 9, title);

		// Set the buttons inside the inventory
		inventory.setItem(0, CustomMaterial.get(Material.ENDER_PEARL, 1, "<#3EED8D>Go to the wild!", "&7Discover the world!", "&7Go to the wild!"));
		inventory.setItem(2, CustomMaterial.get(Material.APPLE, 1, "<#3EED8D>Toggle nametag", "&7Toggle the hearts below player names!"));
		inventory.setItem(4, CustomMaterial.get(Material.NAME_TAG, 1, "<#3EED8D>Toggle Prefix", "&7Toggle the nametags!"));
		inventory.setItem(6, CustomMaterial.get(Material.COMMAND_BLOCK, 1, "<#3EED8D>Spawn Hologram", "&7Spawn a hologrma!"));
		inventory.setItem(8, CustomMaterial.get(Material.PLAYER_HEAD, 1, "<#3EED8D>Random Human!", "&7Check out some info about a random person!"));

		// You can store the entire menu object for the player as his opened inventory
		player.setMetadata("CustomInventory_" + PluginCore.getNamed() + "_Main", new FixedMetadataValue(PluginCore.getInstance(), inventory));
		player.openInventory(inventory);
	}

	public static void human(final Player player) {
		final @NotNull String title = Common.colorizeLegacy("<gradient:#9DED3E:#3EEDBB><bold>Random Human</gradient>");
		final Inventory inventory = Bukkit.createInventory(player, 45, title);

		final JSONObject humanObject = Human.getHumanObject();

		// Set the buttons inside the menu
		inventory.setItem(13, CustomMaterial.get(Material.PLAYER_HEAD, 1, "<#3EED8D><bold>Name: &e" + Human.getName(humanObject), "&6Age: &e" + Human.getAge(humanObject)));
		inventory.setItem(29, CustomMaterial.get(Material.COMPASS, 1, "<#3EED8D><bold>Location", "&6Country&7: &e" + Human.getCountry(humanObject), "&6State&7: &e" + Human.getState(humanObject) + "\n&6City&7: &e" + Human.getCity(humanObject)));
		inventory.setItem(33, CustomMaterial.get(Material.CLOCK, 1, "<#3EED8D><bold>Time & Nat", "&6Timezone&7: &e" + Human.getTimeZone(humanObject), "&6Nationality&7: &e" + Human.getNationality(humanObject)));

		// You can store the entire menu object for the player as his opened inventory
		player.setMetadata("CustomInventory_" + PluginCore.getNamed() + "_Human", new FixedMetadataValue(PluginCore.getInstance(), inventory));
		player.openInventory(inventory);
	}
}
