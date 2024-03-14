package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;


public class QuidditchEventListener implements Listener {
    private JavaPlugin plugin;

    public QuidditchEventListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();

        // Check if the entity has the metadata indicating it's the Snitch
        if (entity.hasMetadata("F_ITEM")) {
            // Broadcast the message
            Bukkit.broadcastMessage(event.getPlayer().getName() + " has caught the Snitch!");

            // Logic for handling the catch, e.g., awarding points, ending the game, etc.

            // Remove the Snitch entities
            // If the entity is an ArmorStand, its parent Bat needs to be removed
            if (entity instanceof ArmorStand && entity.getVehicle() instanceof Bat) {
                Bat bat = (Bat) entity.getVehicle();
                bat.remove(); // This will remove the Bat and, consequently, the ArmorStand
            } else if (entity instanceof Bat) {
                entity.remove(); // Directly remove the Bat if that's what was interacted with
            }

            // Cancel the event to prevent default interactions
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand != null && itemInHand.hasItemMeta() && "Quaffle".equals(itemInHand.getItemMeta().getDisplayName())) {
                // Simulate throwing the Quaffle
                Item quaffle = player.getWorld().dropItem(player.getEyeLocation(), itemInHand.clone());
                quaffle.setVelocity(player.getLocation().getDirection().multiply(1.5)); // Adjust multiplier as needed
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR)); // Remove from player's inventory
                event.setCancelled(true);
            }
        }
    }
}