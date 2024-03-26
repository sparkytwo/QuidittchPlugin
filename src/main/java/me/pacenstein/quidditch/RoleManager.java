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

/**
 * Manages the roles of players in a Quidditch game, including assigning roles and equipping players with role-specific items.
 */
public class RoleManager {
    private final Map<Player, QuidditchRole> playerRoles; // Stores player roles

    /**
     * Constructs a new RoleManager with an empty map of player roles.
     */
    public RoleManager() {
        playerRoles = new HashMap<>();
    }

    /**
     * Sets the role for a player and equips them with role-specific items if necessary.
     *
     * @param player The player whose role is to be set.
     * @param role The role to assign to the player.
     */
    public void setPlayerRole(Player player, QuidditchRole role) {
        playerRoles.put(player, role);
        // Equip the player with role-specific items, if applicable
        if (role == QuidditchRole.BEATER) {
            equipBeater(player);
        }
        player.sendMessage(ChatColor.YELLOW + "You are now a " + role.getDisplayName() + ".");
    }

    /**
     * Equips a player with a Beater bat if their role is Beater.
     *
     * @param player The player to equip.
     */
    private void equipBeater(Player player) {
        ItemStack beaterBat = new ItemStack(Material.STICK); // Use a stick as a placeholder for the Beater bat
        ItemMeta meta = beaterBat.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Beater Bat");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "Use this to deflect Bludgers!");
            meta.setLore(lore);
            beaterBat.setItemMeta(meta);
        }

        player.getInventory().addItem(beaterBat); // Add the Beater bat to the player's inventory
        player.sendMessage(ChatColor.GREEN + "You've been equipped with a Beater Bat!");
    }

    /**
     * Retrieves the role of a given player.
     *
     * @param player The player whose role is to be retrieved.
     * @return The role of the player, or null if the player does not have a role.
     */
    public QuidditchRole getPlayerRole(Player player) {
        return playerRoles.getOrDefault(player, null);
    }

    /**
     * Clears the role of a player, removing them from the role management system.
     *
     * @param player The player whose role is to be cleared.
     */
    public void clearPlayerRole(Player player) {
        playerRoles.remove(player);
        player.sendMessage(ChatColor.YELLOW + "Your Quidditch role has been cleared.");
    }
}
