package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Listener to handle broomstick flight toggling in a Quidditch game plugin.
 *
 * This class listens for player interactions with a specific item designated as a "Quidditch Broomstick"
 * and toggles the player's ability to fly, simulating broomstick flight within the game.
 */
public class BroomstickFlightListener implements Listener {

    /**
     * Handles player interactions, specifically checking for right-click actions with a broomstick to toggle flight.
     *
     * @param event The player interaction event being handled.
     */
    @EventHandler
    public void onPlayerUseBroomstick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        // Validate that the item used is a stick (broomstick), has a name, and is right-clicked
        if (item.getType() == Material.STICK && meta != null && meta.hasDisplayName() &&
                ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quidditch Broomstick") &&
                (event.getAction().toString().contains("RIGHT_CLICK"))) {

            // Toggle flight: disable if currently enabled, or enable if not
            if (player.getAllowFlight()) {
                disableFlight(player); // Disable flight if already flying
            } else {
                enableFlight(player); // Enable flight otherwise
            }
        }
    }

    /**
     * Enables flight for the player, simulating taking off on a broomstick.
     *
     * @param player The player whose flight will be enabled.
     */
    private void enableFlight(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.sendMessage(ChatColor.GREEN + "Broomstick flight enabled! You can now fly.");

        // Implement a timer to automatically disable flight after a set period, if desired
        // Example: Bukkit.getScheduler().runTaskLater(pluginInstance, () -> disableFlight(player), 20L * durationInSeconds);
    }

    /**
     * Disables flight for the player, simulating landing or falling off the broomstick.
     *
     * This method could be called automatically by a timer to limit flight duration.
     *
     * @param player The player whose flight will be disabled.
     */
    private void disableFlight(Player player) {
        if (player.isOnline()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.RED + "Broomstick flight has worn off.");
        }
    }
}
