package com.greazi.plugin.command;

import com.greazi.plugin.Common;
import com.greazi.plugin.settings.Lang;
import com.greazi.plugin.util.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WildCommand implements CommandExecutor, TabCompleter {

	/**
	 * Wild command executor
	 *
	 * @param sender  Source of the command
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    Passed command arguments
	 */
	@Override
	public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

		// Make sure the sender is a player
		if (!(sender instanceof Player)) {
			// Check if the sender is a console
			if (sender instanceof ConsoleCommandSender) {
				if (args.length == 1) {
					// Get the player that needs to be teleported
					final Player target = Bukkit.getPlayer(args[0]);

					// Make sure target is not null
					if (target == null) {
						// Send an error message to the player
						Common.log((Lang.Wild.Other.FAIL).replace("{Player}", args[0]));
						return true;
					}

					// Create a new safe location for the player
					final Location randomLocation = TeleportUtil.findSafeLocation(target);

					// Teleport the target to the random location
					target.teleport(randomLocation);

					// Send a success message to the player
					Common.log((Lang.Wild.Other.Success.SELF).replace("{Player}", target.getName()));

					// Send a success message to the target
					Common.log((Lang.Wild.Other.Success.TARGET).replace("{Player}", sender.getName()));
				} else {
					Common.log(Common.colorizeLegacy("&cUsage: /wild <player>"));
					return true;
				}
			}
			return true;
		}

		final Player player = (Player) sender;

		// When it has no arguments it will teleport you to the wild
		if (args.length < 1) {
			if (!player.hasPermission("greazi.command.wild")) {
				Common.tell(player, Lang.NO_PERMISSION);
				return true;
			}

			// Create a safe location for the player
			final Location randomLocation = TeleportUtil.findSafeLocation(player);

			// Teleport the player to that safe location
			player.teleport(randomLocation);

			// Send a success message to that player
			Common.tell(player, Lang.Wild.Self.SUCCESS);

		}
		// When it has at least 1 argument of a oline player it will teleport that player to the wild
		if (args.length == 1) {
			if (player.hasPermission("greazi.command.wild.others")) {
				// Get the player that needs to be teleported
				final Player target = Bukkit.getPlayer(args[0]);

				// Make sure target is not null
				if (target == null) {
					// Send an error message to the player
					Common.tell(player, (Lang.Wild.Other.FAIL).replace("{Player}", args[0]));
					return true;
				}

				// Create a new safe location for the player
				final Location randomLocation = TeleportUtil.findSafeLocation(target);

				// Teleport the target to the random location
				target.teleport(randomLocation);

				// Send a success message to the player
				Common.tell(player, (Lang.Wild.Other.Success.SELF).replace("{Player}", target.getName()));

				// Send a success message to the target
				Common.tell(target, (Lang.Wild.Other.Success.TARGET).replace("{Player}", player.getName()));
			}
		}

		// Always return true
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
		// Return all online players
		return null;
	}
}
