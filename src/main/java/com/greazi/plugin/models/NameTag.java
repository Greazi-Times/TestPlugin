package com.greazi.plugin.models;

import com.greazi.plugin.Common;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTag {

	// Create a team for the name tag
	private static Team team;
	// Create the scoreboard for the name tag
	private static Scoreboard scoreboard;

	/**
	 * Creates a new {@link NameTag}
	 *
	 * @param player The player to create the name tag for
	 * @param prefix The prefix of the name tag
	 * @param suffix The suffix of the name tag
	 * @param action The action of the name tag
	 */
	public static void setTag(final Player player, final String prefix, final String suffix, final Action action) {
		// Check if the scoreboard is already created
		if (player.getScoreboard() == null || prefix == null || suffix == null || action == null) {
			return;
		}

		// Get the scoreboard of the player
		scoreboard = player.getScoreboard();

		// Check if the player is already in a team
		if (scoreboard.getTeam(player.getName()) == null) {
			// Create a new team for the player
			scoreboard.registerNewTeam(player.getName());
		}

		// Set the team settings for the player
		team = scoreboard.getTeam(player.getName());
		team.setPrefix(Common.colorizeLegacy(prefix));
		team.setSuffix(Common.colorizeLegacy(suffix));
		team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

		// Get the action and switch between them
		switch (action) {
			// Add a new team to the player
			case CREATE:
				team.addPlayer(player);
				break;
			// Update the existing team
			case UPDATE:
				team.unregister();
				scoreboard.registerNewTeam(player.getName());
				team = scoreboard.getTeam(player.getName());
				team.setPrefix(Common.colorizeLegacy(prefix));
				team.setSuffix(Common.colorizeLegacy(suffix));
				team.setNameTagVisibility(NameTagVisibility.ALWAYS);
				team.addPlayer(player);
				break;
			// Remove the team from the player
			case DESTROY:
				team.unregister();
				break;
		}
	}

	/**
	 * The action of the name tag
	 */
	public enum Action {
		CREATE, DESTROY, UPDATE
	}
}
