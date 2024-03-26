package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class BeaterInteractionListener implements Listener {
    private final RoleManager roleManager;

    public BeaterInteractionListener(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @EventHandler
    public void onPlayerUseBat(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemInHand.getItemMeta();

        // Check if the player is a Beater and is holding the correct item
        if (roleManager.getPlayerRole(player) == QuidditchRole.BEATER
                && itemInHand.getType() == Material.STICK
                && meta != null
                && meta.hasDisplayName()
                && meta.getDisplayName().equals(ChatColor.GOLD + "Beater Bat")) {

            player.getNearbyEntities(5, 5, 5).forEach(entity -> { // Adjust the range as needed
                if (entity instanceof Bat) {
                    Bat bludger = (Bat) entity;

                    // Calculate the direction to hit the Bludger
                    Vector direction = player.getLocation().getDirection();
                    direction.multiply(2); // Adjust the multiplier to set the force
                    bludger.setVelocity(direction);

                    // Optional: Play a sound effect or particle effect for feedback
                    player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                }
            });
        }
    }
}
