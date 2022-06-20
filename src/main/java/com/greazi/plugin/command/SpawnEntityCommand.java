package com.greazi.plugin.command;

import com.greazi.plugin.Common;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpawnEntityCommand implements CommandExecutor, TabCompleter {

	/**
	 * Spawn entity command executor
	 *
	 * @param sender  Source of the command
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    Passed command arguments
	 */
	@Override
	public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

		if (!(sender instanceof Player)) {
			if (sender instanceof ConsoleCommandSender) {
				if (args.length < 5)
					return false;

			}
			sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");

			return true;
		}

		if (args.length < 2)
			return false;

		final Player player = (Player) sender;
		final EntityType entityType;

		try {
			entityType = EntityType.valueOf(args[0].toUpperCase());

		} catch (final Throwable t) {
			Common.tell(player, "<red>No such entity by name &l'" + args[0] + "'!");


			return true;
		}

		if (!entityType.isAlive() || !entityType.isSpawnable()) {
			sender.sendMessage(Common.colorize("<red>Entity " + entityType.getName() + " is not alive or spawnable!").toString());

			return true;
		}

		if (args.length == 4) {
			final int x = this.parseNumber(args, 1);
			final int y = this.parseNumber(args, 2);
			final int z = this.parseNumber(args, 3);

			if (x == -1 || y == -1 || z == -1) {
				sender.sendMessage(ChatColor.RED + "Please check your positions, XYZ coordinates must be a whole number! You typed: " + String.join(" ", args));

				return true;
			}

			final Location location = new Location(player.getWorld(), x, y, z);
			player.getWorld().spawnEntity(location, entityType);

		} else if (args.length == 2) {
			final Player targetPlayer = Bukkit.getPlayer(args[1]);

			if (targetPlayer == null) {
				sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' is not online!");

				return true;
			}

			targetPlayer.getWorld().spawnEntity(targetPlayer.getLocation(), entityType);
		}

		return false; // false = player gets to see the Usage message

	}

	private int parseNumber(final String[] args, final int index) {
		try {
			return Integer.parseInt(args[index]);

		} catch (final Throwable t) {
			return -1;
		}
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {

		// Check if the sender is a player
		if (!(sender instanceof Player))
			// Return nothing when the sender is no player
			return new ArrayList<>();

		// Create a new array list
		final List<String> completions = new ArrayList<>();
		// Get the player
		final Player player = (Player) sender;

		// Check what argument we are on
		switch (args.length) {
			// First argument
			case 1:
				// Loop through all the entity types
				for (final EntityType entityType : EntityType.values())
					// Check if the entity type is alive and spawnable
					if (entityType.isSpawnable() && entityType.isAlive()) {
						// Add the entity type to the completions
						final String name = entityType.toString().toLowerCase();

						// Check if the name starts with the argument
						if (name.startsWith(args[0]))
							// Add the name to the completions
							completions.add(name);
					}

				// Break the switch
				break;

			// Second argument
			case 2:
				// Loop through all the players
				for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					// Get the name of the online player
					final String name = onlinePlayer.getName();

					// Check if the name starts with the argument
					if (name.startsWith(args[1]))
						// Add the name to the completions
						completions.add(name);
				}

				// Add the X location of the player
				completions.add(String.valueOf(player.getLocation().getBlockX()));
				// Break the switch
				break;

			// Third argument
			case 3:
				// Add the Y location of the player
				completions.add(String.valueOf(player.getLocation().getBlockY()));

				// Break the switch
				break;

			// Fourth argument
			case 4:
				// Add the Z location of the player
				completions.add(String.valueOf(player.getLocation().getBlockZ()));

				// Break the switch
				break;
		}

		// Return the right arguments
		return completions;
	}
}
