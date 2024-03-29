package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BroomstickFlightListener implements Listener {
    @EventHandler

    public void onPlayerSwitchItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        // Enable flight if the new item or the off-hand item is the broomstick
        if (isBroomstick(newItem) || isBroomstick(offHandItem)) {
            enableFlight(player);
        } else {
            // Disable flight if neither the main hand nor off-hand holds a broomstick
                disableFlight(player);

        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        // Check if the player is trying to fly without holding the broomstick in either hand
        if (!isBroomstick(mainHandItem) && !isBroomstick(offHandItem)) {
            event.setCancelled(true); // Prevent flight if not holding the broomstick
            disableFlight(player);
        }
    }

    private boolean isBroomstick(ItemStack item) {
        if (item != null && item.getType() == Material.STICK && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quidditch Broomstick")) {
                return true;
            }
        }
        return false;
    }

    private void enableFlight(Player player) {
        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.GREEN + "You can now fly with the broomstick.");
        }
    }

    private void disableFlight(Player player) {
        if (player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.RED + "Broomstick flight disabled.");
        }
    }
}