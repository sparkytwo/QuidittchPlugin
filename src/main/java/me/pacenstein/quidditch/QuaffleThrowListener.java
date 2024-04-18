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

    public QuaffleThrowListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!quidditchGame.isGameRunning()) {
            return; // Exit early if the game is not currently active
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!isQuaffle(itemInHand)) {
            return; // Exit early if the item in hand is not a Quaffle
        }

        throwQuaffle(player, itemInHand);
    }

    private void throwQuaffle(Player player, ItemStack quaffle) {
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        player.sendMessage(ChatColor.GREEN + "You've thrown the Quaffle!");

        Item droppedItem = player.getWorld().dropItem(player.getLocation().add(0, 1.5, 0), quaffle);
        droppedItem.setMetadata("Quaffle", new FixedMetadataValue(plugin, true));
        droppedItem.setVelocity(player.getLocation().getDirection().multiply(1.5)); // Simulate throwing force
    }

    private boolean isQuaffle(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Quaffle");
    }
}
