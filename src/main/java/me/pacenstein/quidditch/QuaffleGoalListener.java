package me.pacenstein.quidditch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class QuaffleGoalListener implements Listener {
    private JavaPlugin plugin;
    private QuidditchGame quidditchGame; // Reference to QuidditchGame

    // Constructor modified to accept QuidditchGame instance
    public QuaffleGoalListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerMoveWithQuaffle(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player is carrying the Quaffle
        if (isCarryingQuaffle(player)) {
            // Check if the player is in either team's goal area
            if (quidditchGame.isInGoal(player.getLocation(), "teamA")) {
                plugin.getServer().broadcastMessage(player.getName() + " scores for Team A!");
                // Add any additional logic for handling a goal, e.g., updating scores
            } else if (quidditchGame.isInGoal(player.getLocation(), "teamB")) {
                plugin.getServer().broadcastMessage(player.getName() + " scores for Team B!");
                // Add any additional logic for handling a goal, e.g., updating scores
            }
        }
    }

    private boolean isCarryingQuaffle(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand(); // Check main hand for the Quaffle
        if (itemInHand != null && itemInHand.hasItemMeta()) {
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null && meta.hasDisplayName() && "Quaffle".equals(meta.getDisplayName())) {
                return true; // Player is carrying the Quaffle
            }
        }
        return false; // Player is not carrying the Quaffle
    }
}
