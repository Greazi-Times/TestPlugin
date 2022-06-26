package com.greazi.plugin.listener;

import com.greazi.plugin.Common;
import com.greazi.plugin.PluginCore;
import com.greazi.plugin.models.HealthTag;
import com.greazi.plugin.models.Hologram;
import com.greazi.plugin.models.Menus;
import com.greazi.plugin.models.NameTag;
import com.greazi.plugin.settings.Lang;
import com.greazi.plugin.util.TeleportUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerListener implements Listener {

	/**
	 * The instance of this class
	 */
	@Getter
	private static PlayerListener instance = new PlayerListener();

	/**
	 * The event that gets called when a player joins the server
	 *
	 * @param event PlayerJoinEvent
	 */
	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		// Set the join message to nothing
		event.setJoinMessage("");

		// Get the current machine time
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		final LocalDateTime now = LocalDateTime.now();
		// Get the player from the event
		final Player player = event.getPlayer();

		// Log to console that someone joined the server
		Common.log("Joined player: " + event.getPlayer().getName() + " at " + dtf.format(now));

		// Send everyone a join message
		Common.tellAll("<hover:show_text:\"<gold>Joined at&7: &f{time} <newline>&7<key:key.attack> to send a welcome message\"><click:run_command:Welcome to the server @" + player.getDisplayName() + "><bold>&8[&a+&8] <#5AFFB9>" + player.getDisplayName() + " joined the server!");

		// If the health tag is enabled, create a health tag for the player
		if (PluginCore.getInstance().heartTagEnabled) {
			player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
		} else {
			player.setScoreboard(HealthTag.getHealthBoard());
		}

		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			// Get the vault chat manager
			final Chat chat = PluginCore.getInstance().chat;

			// Set the player's display name to the player's name
			NameTag.setTag(player, chat.getGroupPrefix("world", chat.getPrimaryGroup(player) + " "), chat.getGroupSuffix("world", " " + chat.getPrimaryGroup(player)), NameTag.Action.CREATE);
		} else {
			Common.warn("Vault isn't enabled!");
		}
	}

	/**
	 * The event that gets called when a player leaves the server
	 *
	 * @param event PlayerQuitEvent
	 */
	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		// Get the player from the event
		final Player player = event.getPlayer();

		// Forcefully disable the health tag
		player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
	}

	/**
	 * The event that gets called when a player closes on an inventory
	 *
	 * @param event InventoryCloseEvent
	 */
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event) {
		// Get the player from the event
		final Player player = (Player) event.getPlayer();


		// Check if the player has an open inventory
		if (player.hasMetadata("CustomInventory_" + PluginCore.getNamed() + "_Main") || player.hasMetadata("CustomInventory_" + PluginCore.getNamed() + "_Human")) {
			// Remove the metadata of the menu
			player.removeMetadata("CustomInventory_" + PluginCore.getNamed() + "_Main", PluginCore.getInstance());
			player.removeMetadata("CustomInventory_" + PluginCore.getNamed() + "_Human", PluginCore.getInstance());
		}
	}

	/**
	 * The event that gets called when a player clicks on an inventory
	 *
	 * @param event InventoryClickEvent
	 */
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();


		// Check if it is the main menu
		if (player.hasMetadata("CustomInventory_" + PluginCore.getNamed() + "_Main")) {
			// Cancel the event
			event.setCancelled(true);

			// Check the slot where the player clicked
			if (event.getRawSlot() == 0) {
				// Close the players inventory
				player.closeInventory();
				// Check his permission
				if (!player.hasPermission("greazi.command.wild")) {
					Common.tell(player, Lang.NO_PERMISSION);
					return;
				}

				// Create a safe location for the player
				final Location randomLocation = TeleportUtil.findSafeLocation(player);

				// Teleport the player to that safe location
				player.teleport(randomLocation);

				// Send a success message to that player
				Common.tell(player, Lang.Wild.Self.SUCCESS);

				// Exit the event listener to up performance
				return;
			}
			if (event.getRawSlot() == 2) {
				// Check if the player has permission to use this command
				if (!player.hasPermission("greazi.menu.main.hearts")) {
					// Send no permission message
					Common.tell(player, Lang.NO_PERMISSION);
					// Return true to stop the command from running
				}

				// Check if the HealthTag is enabled
				if (PluginCore.getInstance().heartTagEnabled) {
					// Unregister the heatlhTag
					HealthTag.unRegister();

					// Make sure to remove it from all players
					for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						onlinePlayer.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
					}

					// Tell the player who pressed the button that it is disabled
					Common.tell(player, Lang.NameTag.Sub.DISABLED);

					// Set the heartTagEnabled to false
					PluginCore.getInstance().heartTagEnabled = false;
				} else {
					// Register the healthTag
					HealthTag.register();

					// Make sure to add it to all players
					for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						onlinePlayer.setScoreboard(HealthTag.getHealthBoard());
					}

					// Tell the player who pressed the button that it is enabled
					Common.tell(player, Lang.NameTag.Sub.ENABLED);

					// Set the heartTagEnabled to true
					PluginCore.getInstance().heartTagEnabled = true;
				}

				// Exit the event listener to up performance
				return;
			}
			if (event.getRawSlot() == 4) {
				// Check if the player has permission to use this command
				if (!player.hasPermission("greazi.menu.main.nametag")) {
					// Send no permission message
					Common.tell(player, Lang.NO_PERMISSION);
					// Return true to stop the command from running
				}

				// Check if vault is installed before doing anything else
				if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
					Common.warn("Vault isn't enabled! NameTag support has been disabled!");
					Common.tell(player, "&cSomething is wrong please check console for more details!");
					return;
				}

				final Chat chat = PluginCore.getInstance().chat;

				if (!PluginCore.getInstance().nameTagEnabled) {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						NameTag.setTag(p, chat.getGroupPrefix("world", chat.getPrimaryGroup(p.getPlayer())) + " ", chat.getGroupSuffix("world", chat.getPrimaryGroup(p.getPlayer())) + " ", NameTag.Action.CREATE);
					}
					Common.tell(player, Lang.NameTag.ENABLED);
					PluginCore.getInstance().nameTagEnabled = true;
				} else {
					for (final Player p : Bukkit.getOnlinePlayers()) {
						NameTag.setTag(p, "", "", NameTag.Action.DESTROY);
					}
					Common.tell(player, Lang.NameTag.DISABLED);
					PluginCore.getInstance().nameTagEnabled = false;
				}

				// Exit the event listener to up performance
				return;
			}
			if (event.getRawSlot() == 6) {
				// Check if the player has permission to use this command
				if (!player.hasPermission("greazi.command.hologram")) {
					// Send no permission message
					Common.tell(player, Lang.NO_PERMISSION);
					// Return true to stop the command from running
				}

				// Spawn the hologram
				Hologram.spawn(player);
				// Send success message to the player
				Common.tell(player, Lang.Hologram.SUCCESS);

				// Exit the event listener to up performance
				return;
			}
			if (event.getRawSlot() == 8) {
				// Check if the player has permission to use see the menu
				if (!player.hasPermission("greazi.menu.human")) {
					// Send no permission message
					Common.tell(player, Lang.NO_PERMISSION);
					// Return true to stop the command from running
				}

				// Make sure to close the current inv before opening a new one
				player.closeInventory();

				// Open the human inventory
				Menus.human(player);

				// Exit the event listener to up performance
				return;
			}
		}

		// Check if it is the human menu
		if (player.hasMetadata("CustomInventory_" + PluginCore.getNamed() + "_Human")) {
			event.setCancelled(true);
		}
	}
}
