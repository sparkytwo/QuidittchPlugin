package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIEventListener implements Listener {

    private QuidditchGame quidditchGame;

    public GUIEventListener(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        String inventoryTitle = event.getView().getTitle(); // Correctly retrieve the inventory title

        // Only cancel clicks in the top inventory if it is one of the custom GUIs
        if (topInventory != null && clickedInventory != null && clickedInventory.equals(topInventory)) {
            switch (inventoryTitle) {
                case "Choose Competing Houses":
                    handleHouseSelection(event, player);
                    break;
                case "Select House":
                    handleTeamSelection(event, player);
                    break;
                case "Select Class":
                    handleClassSelection(event, player);
                    break;
                default:
                    break;
            }
            event.setCancelled(true);  // Cancel events only within custom GUIs
        }
        // Allow normal handling in the player's own inventory
    }


    private void handleHouseSelection(InventoryClickEvent event, Player player) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta().getDisplayName() == null) {
            player.sendMessage(ChatColor.RED + "Invalid item.");
            return;
        }

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        player.sendMessage(ChatColor.YELLOW + "You clicked: " + itemName); // Debug output

        try {
            HogwartsHouse selectedHouse = HogwartsHouse.valueOf(itemName.toUpperCase().replace(" ", "_"));
            quidditchGame.addHouseSelection(player, selectedHouse);
            player.sendMessage(ChatColor.GREEN + "Selected: " + selectedHouse); // Confirm selection
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid selection. Caught Exception.");
        }
    }

    private void handleTeamSelection(InventoryClickEvent event, Player player) {
        System.out.println("Handling team selection...");
        String itemName = getItemName(event.getCurrentItem());
        if (itemName == null) {
            System.out.println("No item name found.");
            return;
        }

        System.out.println("Item selected: " + itemName);
        HogwartsHouse selectedHouse = determineHouseFromItem(itemName);

        if (selectedHouse != null && (selectedHouse == quidditchGame.house1 || selectedHouse == quidditchGame.house2)) {
            System.out.println("Assigning player to house: " + selectedHouse);
            quidditchGame.assignPlayerToHouse(player, selectedHouse);
            player.closeInventory();
            quidditchGame.openClassSelectionGUI(player);
        }
    }

    private void handleClassSelection(InventoryClickEvent event, Player player) {
        String itemName = getItemName(event.getCurrentItem());
        if (itemName == null) return;

        if (itemName.startsWith("Choose ")) {
            quidditchGame.assignPlayerClass(player, itemName.substring(7)); // "Choose " is 7 characters long
            player.closeInventory(); // Close inventory after selecting a class
        }
    }

    private String getItemName(ItemStack item) {
        if (item == null || !item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) return null;
        return ChatColor.stripColor(item.getItemMeta().getDisplayName());
    }
    private HogwartsHouse determineHouseFromItem(String itemName) {
        if (itemName.contains("Gryffindor")) {
            return HogwartsHouse.GRYFFINDOR;
        } else if (itemName.contains("Ravenclaw")) {
            return HogwartsHouse.RAVENCLAW;
        } else if (itemName.contains("Hufflepuff")) {
            return HogwartsHouse.HUFFLEPUFF;
        } else if (itemName.contains("Slytherin")) {
            return HogwartsHouse.SLYTHERIN;
        } else {
            return null; // Return null if no matching house is found
        }
    }


}
