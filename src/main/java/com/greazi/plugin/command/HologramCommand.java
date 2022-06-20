package com.greazi.plugin.command;

import com.greazi.plugin.Common;
import com.greazi.plugin.models.Hologram;
import com.greazi.plugin.settings.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HologramCommand implements CommandExecutor, TabCompleter {

	/**
	 * Hologram command executor
	 *
	 * @param sender  Source of the command
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    Passed command arguments
	 */
	@Override
	public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

		// Check if the sender is a player
		if (!(sender instanceof Player)) {
			// Send fail message to the sender
			sender.sendMessage(Lang.Command.NO_CONSOLE);

			// Return true to stop the command from running
			return true;
		}

		// Get the player
		final Player player = (Player) sender;

		// Check if the player has permission to use this command
		if (!player.hasPermission("greazi.command.hologram")) {
			// Send no permission message
			Common.tell(player, Lang.NO_PERMISSION);
			// Return true to stop the command from running
			return true;
		}

		// Spawn the hologram
		Hologram.spawn(player);
		// Send success message to the player
		Common.tell(player, Lang.Hologram.SUCCESS);

		// Return true to stop the command from running
		return true;
	}

	/**
	 * Tab completion for the /hologram command
	 *
	 * @param sender  Source of the command.  For players tab-completing a
	 *                command inside of a command block, this will be the player, not
	 *                the command block.
	 * @param command Command which was executed
	 * @param alias   Alias of the command which was used
	 * @param args    The arguments passed to the command, including final
	 *                partial argument to be completed
	 * @return
	 */
	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
		// Return nothing to tab complete
		return new ArrayList<>();
	}
}
