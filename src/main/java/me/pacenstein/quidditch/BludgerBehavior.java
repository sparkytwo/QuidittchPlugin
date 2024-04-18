package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
     */
    public BludgerBehavior(JavaPlugin plugin, Bat bludgerBat) {
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
            // Additional check to ensure the player is indeed the target and within a more precise range if needed
            if (player.equals(target)) {
                // Check if the player is holding a Quaffle
                if (isHoldingQuaffle(player)) {
                    // Drop the Quaffle
                    dropQuaffle(player);

                    // Broadcast message
                    Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " has dropped the Quaffle due to a Bludger hit!");

                    // Reset target to null to select a new target next tick
                    target = null;
                }

                // Logic to disable the player's ability to fly, as before
                if (player.getAllowFlight()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(ChatColor.RED + "You've been hit by a Bludger! Remount your broom to fly again.");
                }
            }
        }
    }

    private boolean isHoldingQuaffle(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        return itemInHand != null && itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasDisplayName()
                && ChatColor.stripColor(itemInHand.getItemMeta().getDisplayName()).equalsIgnoreCase("Quaffle");
    }

    private void dropQuaffle(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (isHoldingQuaffle(player)) {
            // Drop the item in the world
            player.getWorld().dropItemNaturally(player.getLocation(), itemInHand);
            // Remove the item from the player's hand
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
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
