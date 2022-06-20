package com.greazi.plugin.models;

import com.greazi.plugin.Common;
import com.greazi.plugin.PluginCore;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class Hologram {

	/**
	 * Spawn an animated hologram
	 *
	 * @param player Player to spawn the hologram for
	 */
	public static void spawn(final Player player) {
		// Create a new armor-stand
		final ArmorStand hologram = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		// Set the armor-stand settings for a hologram
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		// Set the spawn name of the hologram
		hologram.setCustomName(Common.colorizeLegacy("<gradient:green:blue>TechsCode</gradient>"));

		// Make a new schedule to refresh the holograms name
		final BukkitScheduler scheduler = PluginCore.getInstance().getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(PluginCore.getInstance(), new Runnable() {
			int count = 0;

			// Make a gradient effect for the hologram name
			// Really ugly but the only way I know how to do this as I have never worked with gradients before
			@Override
			public void run() {
				count++;
				if (count == 1)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 2)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 3)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 4)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 5)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 6)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 7)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9:#FFFFFF>TechsCode</gradient>"));
				if (count == 8)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE:#A2FFE9>TechsCode</gradient>"));
				if (count == 9)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2:#48FFDE>TechsCode</gradient>"));
				if (count == 10)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE:#00FFC2>TechsCode</gradient>"));
				if (count == 11)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9:#48FFDE>TechsCode</gradient>"));
				if (count == 12)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#A2FFE9>TechsCode</gradient>"));
				if (count == 13)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 14)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 15)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 16)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 17)
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
				if (count == 18) {
					hologram.setCustomName(Common.colorizeLegacy("<gradient:#48FFDE:#A2FFE9:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF:#FFFFFF>TechsCode</gradient>"));
					count = 0;
				}
			}
		}, 0L, 2L);

	}
}
