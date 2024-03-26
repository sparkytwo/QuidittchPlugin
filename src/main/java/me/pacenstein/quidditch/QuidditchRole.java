package me.pacenstein.quidditch;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the various roles a player can assume in a Quidditch game.
 * Each role has a distinct function and set of responsibilities within the game.
 */
public enum QuidditchRole {
    CHASER("Chaser"),   // Role responsible for scoring goals by throwing the Quaffle through the opponent's hoops
    BEATER("Beater"),   // Protects teammates from Bludgers and attempts to knock Bludgers towards opponents
    KEEPER("Keeper"),   // Guards the goal hoops to prevent the opposing team from scoring
    SEEKER("Seeker");   // Attempts to catch the Golden Snitch, which ends the game and scores significant points

    private final String displayName; // The human-readable name of the role
    private final Map<Player, QuidditchRole> playerRoles = new HashMap<>();

    /**
     * Constructor for the enum to set the human-readable name of the role.
     *
     * @param displayName The name of the role as it should be displayed to users.
     */
    QuidditchRole(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the role.
     *
     * @return The human-readable name of the role.
     */
    public String getDisplayName() {
        return displayName;
    }
}
