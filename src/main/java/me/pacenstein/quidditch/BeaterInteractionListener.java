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

/**
 * Listener for beater interactions in a Quidditch game.
 *
 * This class listens to player interactions, specifically targeting the role
 * of Beaters in Quidditch. Beaters can hit Bludgers (represented by bats) towards other players.
 */
public class BeaterInteractionListener implements Listener {
    private final RoleManager roleManager;

    /**
     * Constructor to set up the listener with a RoleManager.
     *
     * @param roleManager The role manager handling player roles within the game.
     */
    public BeaterInteractionListener(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     * Handles the event where a player uses the Beater bat.
     *
     * This method checks if the player is a Beater and holding a Beater Bat. If so,
     * it applies a velocity to nearby Bludgers (bats), simulating hitting them.
     *
     * @param event The player interaction event.
     */
    @EventHandler
    public void onPlayerUseBat(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemInHand.getItemMeta();

        // Validate if the player role is Beater, they are holding a stick, and it's named "Beater Bat"
        if (roleManager.getPlayerRole(player) == QuidditchRole.BEATER
                && itemInHand.getType() == Material.STICK
                && meta != null
                && meta.hasDisplayName()
                && meta.getDisplayName().equals(ChatColor.GOLD + "Beater Bat")) {

            // Iterate over nearby entities within a 5-block radius to find Bludgers
            player.getNearbyEntities(5, 5, 5).forEach(entity -> {
                if (entity instanceof Bat) {
                    Bat bludger = (Bat) entity;

                    // Set the velocity towards the direction the player is looking, simulating a hit
                    Vector direction = player.getLocation().getDirection();
                    direction.multiply(2); // Control the force of the hit by adjusting the multiplier
                    bludger.setVelocity(direction);

                    // Optional: Play a sound for feedback, indicating the action was successful
                    player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                }
            });
        }
    }
}
