package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleManager {
    private final Map<Player, QuidditchRole> playerRoles;

    public RoleManager() {
        playerRoles = new HashMap<>();
    }

    public void setPlayerRole(Player player, QuidditchRole role) {
        playerRoles.put(player, role);
        if (role == QuidditchRole.BEATER) {
            equipBeater(player); // This is where you call the method to equip the Beater bat
        }
        player.sendMessage("You are now a " + role.getDisplayName());
    }

    private void equipBeater(Player player) {
        ItemStack beaterBat = new ItemStack(Material.STICK); // Use STICK as the base item for the bat
        ItemMeta meta = beaterBat.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Beater Bat"); // Set a custom name for the bat
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "Use this to deflect Bludgers!");
            meta.setLore(lore);
            beaterBat.setItemMeta(meta);
        }

        // Optionally clear the current item in hand, or find a safe spot in the inventory
        player.getInventory().addItem(beaterBat); // Give the player the Beater bat
        player.sendMessage(ChatColor.GREEN + "You've been equipped with a Beater Bat!");
    }
    public QuidditchRole getPlayerRole(Player player) {
        return playerRoles.getOrDefault(player, null);
    }

    public void clearPlayerRole(Player player) {
        playerRoles.remove(player);
        player.sendMessage("Your Quidditch role has been cleared.");
    }


}
