package com.greazi.plugin.command;

import com.greazi.plugin.Common;
import com.greazi.plugin.PluginCore;
import com.greazi.plugin.models.Hologram;
import com.greazi.plugin.models.Menus;
import com.greazi.plugin.settings.Lang;
import com.greazi.plugin.settings.SimpleSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GreaziCoreCommand implements CommandExecutor, TabCompleter {

	/**
	 * GreaziCore command executor
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
			// If the sender is a console, check if the command has enough arguments
			if (sender instanceof ConsoleCommandSender) {
				// TODO: Finish this part
				return false;
			}

			return true;
		}

		// Set the player
		final Player player = (Player) sender;

		// Check if the command has 0 arguments
		if (args.length < 1) {
			// Send the main info of the command
			Common.tell(player, "&7" + Common.chatLine());
			Common.tell(player, "&r    &6Greazi Core Version&7: &f" + PluginCore.getVersion());
			Common.tell(player, "&r    &6Author&7: &f" + PluginCore.getInstance().getDescription().getAuthors().get(0));
			Common.tell(player, "&7" + Common.chatLine());

			// Return true to stop the command from running
			return true;
		}
		// Check if the command has 1 argument
		if (args.length < 2) {
			// Check the first argument
			switch (args[0].toLowerCase()) {
				// Check if it is "reload"
				case "reload":
					// Reload all the files
					PluginCore.getInstance().reloadConfig();
					SimpleSettings.load();
					Lang.load();
					// Send reload message
					Common.tell(player, "<green>" + Lang.Command.RELOADING);

					// Break the switch so it doesn't break
					break;
				// Check if it is "help" of "?"
				case "help":
				case "?":
					// Send a help menu
					Common.tell(player, "&7" + Common.chatLineSmooth());
					Common.tell(player, "&r    &6Greazi Core Version&7: &f" + PluginCore.getVersion());
					Common.tell(player, "&r    &6Author&7: &f" + PluginCore.getInstance().getDescription().getAuthors().get(0));
					Common.tell(player, "&r ");
					Common.tell(player, "&r    /greazi help: shows this help");
					Common.tell(player, "&r    /greazi reload: reloads the config");
					Common.tell(player, "&7" + Common.chatLineSmooth());

					// Break the switch so it doesn't break
					break;
				// Check if it is "minimessage"
				case "minimessage":
					// Send the input for minimessages
					player.sendMessage(SimpleSettings.MINIMESSAGE_TEST);
					// Send the colorized text with minimessages
					Common.tell(player, SimpleSettings.MINIMESSAGE_TEST);

					// Break the switch so it doesn't break
					break;
				// Check if it is "hologram"
				case "hologram":
					if (!player.hasPermission("greazi.command.hologram")) {
						Common.tell(player, Lang.NO_PERMISSION);
						return true;
					}
					// Spawn the hologram
					Hologram.spawn(player);
					// Send success message
					Common.tell(player, Lang.Hologram.SUCCESS);

					// Break the switch so it doesn't break
					return true;
				// Check if it is "menu"
				case "menu":
					// Get the main menu for the player and open it
					Menus.main(player);

					// Break the switch so it doesn't break
					break;
				// Check if it is "merch"
				case "merch":
					// Create a merchat shop
					final Merchant merchant = Bukkit.createMerchant(Common.colorizeLegacy("&6&lGreazi is sell some stuff"));
					// Make list for all the items
					final List<MerchantRecipe> recipes = new ArrayList<>();

					// Add an item
					final MerchantRecipe first = new MerchantRecipe(new ItemStack(Material.DIAMOND, 5), 1);
					// Set the required item
					first.addIngredient(new ItemStack(Material.BREAD, 20));
					// Add it to the recipes list
					recipes.add(first);

					// Add a second item
					final MerchantRecipe second = new MerchantRecipe(new ItemStack(Material.ARROW, 1), 999);
					// Set the required item
					second.addIngredient(new ItemStack(Material.APPLE, 3));
					// Add it to the recipes list
					recipes.add(second);

					// Add a third item
					final MerchantRecipe third = new MerchantRecipe(new ItemStack(Material.GLASS_BOTTLE, 3), 999);
					// Set the required item
					third.addIngredient(new ItemStack(Material.GLASS, 1));
					// Add it to the recipes list
					recipes.add(third);

					// Set the recipes
					merchant.setRecipes(recipes);

					// Open the merchant shop
					player.openMerchant(merchant, true);

					// Break the switch so it doesn't break
					break;
			}

		}
		// Always return true
		return true;
	}

	/**
	 * Tab completion for the GreaziCore command
	 *
	 * @param sender  Source of the command.  For players tab-completing a
	 *                command inside of a command block, this will be the player, not
	 *                the command block.
	 * @param command Command which was executed
	 * @param label   Alias of the command which was used
	 * @param args    The arguments passed to the command, including final
	 *                partial argument to be completed
	 * @return A list of possible completions for the final argument
	 */
	@Override
	public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {

		// Check if the sender is a player
		if (!(sender instanceof Player))
			// Return nothing when the sender is no player
			return new ArrayList<>();

		// Create a new array list
		final List<String> completions = new ArrayList<>();

		// Check what argument we are on
		switch (args.length) {
			// Return the possible arguments when the argument is 1
			case 1:
				completions.add("help");
				completions.add("reload");
				completions.add("minimessage");
				completions.add("merch");
				completions.add("hologram");
				completions.add("menu");
				break;
			case 2:
				if (args[0].equalsIgnoreCase("help")) {
					completions.add("test");
				}
				break;
		}

		// Return the right arguments
		return completions;
	}
}
