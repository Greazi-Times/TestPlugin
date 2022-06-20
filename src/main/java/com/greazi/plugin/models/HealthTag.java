package com.greazi.plugin.models;

import com.greazi.plugin.Common;
import com.greazi.plugin.debug.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class HealthTag {

	private static Scoreboard scoreboard;

	/**
	 * Creates a new {@link HealthTag}
	 */
	public static void register() {

		// A debugger to see that this method is called
		Debugger.debug("HealthTag", "Registering HealthTag");

		// Create a new scoreboard
		final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		final Scoreboard healthBoard = scoreboardManager.getNewScoreboard();
		final Objective h = healthBoard.registerNewObjective("healthTag", Criterias.HEALTH);

		// Set all the information about the scoreboard
		h.setDisplaySlot(DisplaySlot.BELOW_NAME);
		h.setDisplayName(Common.colorizeLegacy("&c&l❤"));

		// Save the scoreboard in the system
		scoreboard = healthBoard;
		Debugger.debug("HealthTag", "HealthTag == " + scoreboard);
	}

	/**
	 * Unregisters the {@link HealthTag}
	 */
	public static void unRegister() {
		// Check if the scoreboard is already created
		if (scoreboard.getObjective("healthTag") != null) {
			// Unregister the scoreboard
			scoreboard.getObjective("healthTag").unregister();

			Debugger.debug("HealthTag", "HealthTag unregistered");
		} else {
			Debugger.debug("HealthTag", "HealthTag is not registered");
		}
	}

	public static boolean isEnabled() {
		Debugger.debug("HealthTag", "Checking if HealthTag is enabled; " + scoreboard);
		return scoreboard.getObjective("healthTag") != null;
	}

	public static Scoreboard getHealthBoard() {
		return scoreboard;
	}
}
