package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
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
        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = event.getView().getTitle();

        if (inventoryTitle.equals("Select Team")) {
            event.setCancelled(true); // Prevent taking items from the inventory

            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            // Handle team selection and then open the class selection GUI
            if (itemName.equals("Join Team A")) {
                quidditchGame.assignPlayerToTeam(player, "TeamA");
                quidditchGame.openClassSelectionGUI(player); // Open class selection after choosing a team
            } else if (itemName.equals("Join Team B")) {
                quidditchGame.assignPlayerToTeam(player, "TeamB");
                quidditchGame.openClassSelectionGUI(player); // Open class selection after choosing a team
            }
        } else if (inventoryTitle.equals("Select Class")) {
            event.setCancelled(true); // Prevent taking items from the inventory

            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            // Handle class selection
            if (itemName.equals("Choose Chaser") || itemName.equals("Choose Beater") || itemName.equals("Choose Keeper") || itemName.equals("Choose Seeker")) {
                quidditchGame.assignPlayerClass(player, itemName.substring(7)); // "Choose " is 7 characters long
                player.closeInventory(); // Close inventory after selecting a class
            }
        }
    }
}
