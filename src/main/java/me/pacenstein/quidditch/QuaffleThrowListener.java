package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * This class listens for player interactions, specifically for actions indicative of a player throwing the Quaffle.
 * It manages the logic of determining if the player is holding a Quaffle and, if so, processes its throwing.
 */
public class QuaffleThrowListener implements Listener {
    private JavaPlugin plugin;
    private QuidditchGame quidditchGame; // Reference to the main Quidditch game class

    /**
     * Constructor for initializing the QuaffleThrowListener with a reference to the plugin and the game.
     *
     * @param plugin The plugin instance.
     * @param quidditchGame The instance of the QuidditchGame containing game logic.
     */
    public QuaffleThrowListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    /**
     * Handles the event where a player interacts with an item, specifically looking to see if they're
     * attempting to throw the Quaffle.
     *
     * @param event The player interaction event.
     */
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the Quidditch game is currently active
        if (!quidditchGame.isGameRunning()) {
            //player.sendMessage(ChatColor.RED + "The game is not currently running.");
            return; // Early exit if the game is not running
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        // Validate the item to ensure it's a Quaffle
        if (isQuaffle(item)) {
            // Clear the player's hand and notify them
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            player.sendMessage(ChatColor.GREEN + "You've thrown the Quaffle!");

            // Drop the item in the world as the thrown Quaffle, giving it forward momentum
            Item droppedItem = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), item);
            droppedItem.setMetadata("Quaffle", new FixedMetadataValue(plugin, true));
            Vector playerDirection = player.getLocation().getDirection();
            droppedItem.setVelocity(playerDirection.multiply(1.5)); // Apply forward momentum to simulate throwing
        }
    }

    /**
     * Checks if the given ItemStack is a Quaffle based on its metadata.
     *
     * @param item The ItemStack to check.
     * @return true if the item is a Quaffle; false otherwise.
     */
    private boolean isQuaffle(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quaffle");
        }
        return false;
    }
}
