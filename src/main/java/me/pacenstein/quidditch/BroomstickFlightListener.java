package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BroomstickFlightListener implements Listener {

    @EventHandler
    public void onPlayerUseBroomstick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        // Check if the player is right-clicking with a broomstick
        if (item.getType() == Material.STICK && meta != null && meta.hasDisplayName() &&
                ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quidditch Broomstick") &&
                (event.getAction().toString().contains("RIGHT_CLICK"))) {

            // Check if the player already has flight enabled to toggle it off
            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "Broomstick flight disabled!");
            } else {
                enableFlight(player);
            }
        }
    }

    private void enableFlight(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage(ChatColor.GREEN + "Broomstick flight enabled! You can now fly.");

        // Optional: Implement a timer to disable flight after a certain period
        // Bukkit.getScheduler().runTaskLater(YourPluginInstance, () -> disableFlight(player), 20L * durationInSeconds);
    }

    // Optional method to disable flight, can be called by a timer
    private void disableFlight(Player player) {
        if (player.isOnline()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.RED + "Broomstick flight has worn off.");
        }
    }
}
