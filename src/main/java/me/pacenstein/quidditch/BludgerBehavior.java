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

public class BludgerBehavior extends BukkitRunnable {
    private final JavaPlugin plugin;
    private final Bat bludgerBat;
    private Player target;

    public BludgerBehavior(JavaPlugin plugin, Bat bludgerBat, ArmorStand armorStand) {
        this.plugin = plugin;
        this.bludgerBat = bludgerBat;
        this.target = null;
    }

    @Override
    public void run() {
        // Target selection
        if (target == null || !target.isOnline()) {
            target = selectRandomPlayer();
        }

        if (target != null) {
            // Movement towards target
            Location bludgerLocation = bludgerBat.getLocation();
            Location targetLocation = target.getLocation();
            Vector direction = targetLocation.toVector().subtract(bludgerLocation.toVector()).normalize();
            bludgerBat.setVelocity(direction.multiply(1.0)); // Increased speed
        }

        checkBludgerHit();
    }



    private void checkBludgerHit() {
        // Assuming a hit radius of 2 blocks for simplicity
        double hitRadius = 2.0;
        Location bludgerLocation = bludgerBat.getLocation();

        // Check for nearby players
        Collection<Player> nearbyPlayers = bludgerLocation.getNearbyPlayers(hitRadius);
        for (Player player : nearbyPlayers) {
            // Logic to prevent hitting the same player too frequently, if desired

            // Disable the player's flight
            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "You've been hit by a Bludger! Remount your broom to fly again.");

                // Optional: Implement a cooldown before the player can fly again
            }
        }
    }

    private Player selectRandomPlayer() {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        int size = players.size();
        if (size > 0) {
            int item = new Random().nextInt(size);
            int i = 0;
            for (Player player : players) {
                if (i == item) {
                    return player; // This should be modified to ensure the player is part of the game and not already a target
                }
                i++;
            }
        }
        return null;
    }
}
