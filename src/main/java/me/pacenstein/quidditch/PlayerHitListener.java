package me.pacenstein.quidditch;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerHitListener implements Listener {
    private JavaPlugin plugin;
    private QuidditchGame quidditchGame;

    public PlayerHitListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return; // Only interested in players being hit
        }

        Player player = (Player) event.getEntity();
        if (isCarryingQuaffle(player)) {
            dropQuaffle(player);
            plugin.getServer().broadcastMessage(player.getName() + " has dropped the Quaffle due to being hit!");
        }
    }

    private boolean isCarryingQuaffle(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        return itemInHand != null && itemInHand.hasItemMeta() &&
                itemInHand.getItemMeta().hasDisplayName() &&
                "Quaffle".equals(itemInHand.getItemMeta().getDisplayName());
    }

    private void dropQuaffle(Player player) {
        ItemStack quaffle = getQuaffle();
        player.getInventory().removeItem(quaffle);
        player.getWorld().dropItemNaturally(player.getLocation(), quaffle);
    }

    private ItemStack getQuaffle() {
        ItemStack quaffle = new ItemStack(Material.LEATHER); // Assuming LEATHER represents the Quaffle
        quaffle.getItemMeta().setDisplayName("Quaffle");
        return quaffle;
    }
}
