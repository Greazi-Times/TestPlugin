package com.greazi.plugin.util;

import com.greazi.plugin.settings.SimpleSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtil {

	/**
	 * Create a list of unsafe blocks
	 */
	public static HashSet<Material> bad_blocks = new HashSet<>();

	/*
	 * Add all unsafe blocks to the list
	 */
	static {
		bad_blocks.add(Material.LAVA);
		bad_blocks.add(Material.FIRE);
		bad_blocks.add(Material.CACTUS);
	}

	/**
	 * Find the safe location for the player
	 *
	 * @param player The player to be teleported
	 * @return A safe location for the player to be teleported to
	 */
	public static Location findSafeLocation(final Player player) {

		// Create a new random location
		Location randomLocation = generateLocation(player);

		// Check if the location is safe
		while (!isLocationSafe(randomLocation)) {
			// If not look for a new one
			randomLocation = generateLocation(player);
		}
		// Return a safe location
		return randomLocation;
	}

	/**
	 * Create a safe location for the player to be teleported to
	 *
	 * @param player The player to be teleported
	 * @return A safe location for the player to be teleported to
	 */
	public static Location generateLocation(final Player player) {
		// Setup a new random number generator
		final Random random = new Random();

		// Set all values to 0
		int x = 0;
		int y = 150;
		int z = 0;

		// Get the Border settings value
		final int border = SimpleSettings.WILD_BORDER;

		// Check if the border is greater than 0 and limit it to that value
		if (!(border <= 0)) {
			// Generate a random number below the border
			x = random.nextInt(border);
			z = random.nextInt(border);
			// Border is disabled so use the max value of 25000
		} else {
			// Generate a border below the default value of 25000
			x = random.nextInt(25000);
			z = random.nextInt(25000);
		}

		// Get the location from the values generated above
		final Location randomLocation = new Location(player.getWorld(), x, y, z);
		// Set the Y value to the highest block below the player
		y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
		// Set the Y location
		randomLocation.setY(y);

		// Return the location
		return randomLocation;
	}

	/**
	 * Check if the location is safe
	 *
	 * @param location The location to check
	 * @return True if the location is safe, false if not
	 */
	public static boolean isLocationSafe(final Location location) {

		// Get the block at the location
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();
		// Get the world and get the block at the location
		final Block block = location.getWorld().getBlockAt(x, y, z);
		final Block below = location.getWorld().getBlockAt(x, y - 1, z);
		final Block above = location.getWorld().getBlockAt(x, y + 1, z);

		// Check if that block isn't a bad block
		return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
	}

}
