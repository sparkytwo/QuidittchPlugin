package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Random;

/**
 * This class defines the behavior of a Bludger in the Quidditch game.
 * It targets players and moves towards them to simulate being hit by the Bludger.
 * Upon getting close enough to a player, certain effects are applied to mimic the impact.
 */
public class BludgerBehavior extends BukkitRunnable {
    private final JavaPlugin plugin; // Reference to the plugin for accessing server functionalities
    private final Bat bludgerBat;    // The Bludger entity
    private Player target;           // The current target player

    /**
     * Constructs a new BludgerBehavior instance.
     *
     * @param plugin      The plugin instance
     * @param bludgerBat  The bat entity representing the Bludger
     * @param armorStand  An unused parameter in this context, potentially for future use or visual effects
     */
    public BludgerBehavior(JavaPlugin plugin, Bat bludgerBat, ArmorStand armorStand) {
        this.plugin = plugin;
        this.bludgerBat = bludgerBat;
        this.target = null; // Initialize with no target
    }

    @Override
    public void run() {
        // Periodically check and update the Bludger's behavior
        if (target == null || !target.isOnline()) {
            // Select a new target if the current one is null or not online
            target = selectRandomPlayer();
        }

        // Move the Bludger towards the target if one exists
        if (target != null) {
            Location bludgerLocation = bludgerBat.getLocation();
            Location targetLocation = target.getLocation();
            Vector direction = targetLocation.toVector().subtract(bludgerLocation.toVector()).normalize();
            bludgerBat.setVelocity(direction); // Move the Bludger towards the target
        }

        // Check if the Bludger has hit any players
        checkBludgerHit();
    }

    /**
     * Checks if the Bludger has hit a player.
     * If so, it applies effects to simulate the impact of the hit.
     */
    private void checkBludgerHit() {
        double hitRadius = 2.0; // Define a radius within which players are considered hit
        Location bludgerLocation = bludgerBat.getLocation();
        Collection<Player> nearbyPlayers = bludgerLocation.getNearbyPlayers(hitRadius);

        for (Player player : nearbyPlayers) {
            // Prevent the same player from being hit too frequently, if necessary

            // Disable the player's ability to fly, simulating being knocked off their broom
            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "You've been hit by a Bludger! Remount your broom to fly again.");
                // Consider implementing a cooldown before the player can fly again
            }
        }
    }

    /**
     * Selects a random player from the online players as the Bludger's new target.
     *
     * @return A randomly selected Player, or null if no players are online.
     */
    private Player selectRandomPlayer() {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        int size = players.size();
        if (size > 0) {
            int item = new Random().nextInt(size);
            int i = 0;
            for (Player player : players) {
                if (i == item) {
                    return player; // Future improvement: ensure the player is part of the game
                }
                i++;
            }
        }
        return null; // Return null if no suitable player was found
    }
}
