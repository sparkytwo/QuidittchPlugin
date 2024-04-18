package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BroomstickFlightListener implements Listener {

    @EventHandler
    public void onPlayerSwitchItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Ignore this logic if the player is in Creative mode
        }

        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (isBroomstick(newItem) || isBroomstick(offHandItem)) {
            enableFlight(player);
        } else {
            disableFlight(player);
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Do not interfere with flight in Creative mode
        }

        if (!isHoldingBroomstick(player)) {
            event.setCancelled(true);
            disableFlight(player);
        }
    }

    private boolean isHoldingBroomstick(Player player) {
        return isBroomstick(player.getInventory().getItemInMainHand()) ||
                isBroomstick(player.getInventory().getItemInOffHand());
    }

    private boolean isBroomstick(ItemStack item) {
        if (item == null || item.getType() != Material.STICK || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quidditch Broomstick");
    }

    private void enableFlight(Player player) {
        if (player.getGameMode() != GameMode.CREATIVE && !player.getAllowFlight()) {
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.GREEN + "You can now fly with the broomstick.");
        }
    }

    private void disableFlight(Player player) {
        if (player.getGameMode() != GameMode.CREATIVE && player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.RED + "Broomstick flight disabled.");
        }
    }
}
