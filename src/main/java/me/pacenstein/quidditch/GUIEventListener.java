package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIEventListener implements Listener {

    private QuidditchGame quidditchGame;

    public GUIEventListener(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Select Team and Class")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            // Handle team selection with if-else
            if (itemName.equals("Join Team A")) {
                quidditchGame.assignPlayerToTeam(player, "TeamA");
            } else if (itemName.equals("Join Team B")) {
                quidditchGame.assignPlayerToTeam(player, "TeamB");
            }

            // Handle class selection with if-else
            if (itemName.equals("Choose Chaser")) {
                quidditchGame.assignPlayerClass(player, "CHASER");
            } else if (itemName.equals("Choose Beater")) {
                quidditchGame.assignPlayerClass(player, "BEATER");
            } else if (itemName.equals("Choose Keeper")) {
                quidditchGame.assignPlayerClass(player, "KEEPER");
            } else if (itemName.equals("Choose Seeker")) {
                quidditchGame.assignPlayerClass(player, "SEEKER");
            }

            player.closeInventory();
        }
    }
}
