package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Executes commands related to setting or clearing a player's role in Quidditch.
 * Allows players to select their Quidditch role or clear their current role assignment.
 */

public class RoleCommandExecutor {
    ;
    /*private final RoleManager roleManager;
    private final QuidditchGame quidditchGame;

    /**
     * Constructs a RoleCommandExecutor with references to the role manager and Quidditch game instances.
     *
     * @param roleManager The role manager handling role assignments.
     * @param game The Quidditch game instance.
     */
    /*public RoleCommandExecutor(RoleManager roleManager, QuidditchGame game) {
        this.roleManager = roleManager;
        this.quidditchGame = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure the command is executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        // Handle the command to clear a player's Quidditch role
        if ("clearclass".equalsIgnoreCase(command.getName())) {
            roleManager.clearPlayerRole(player);
            player.sendMessage(ChatColor.YELLOW + "Your Quidditch role has been cleared.");
            return true;
        }

        // Handle the command to set a player's Quidditch role
        if (args.length > 0) {
            try {
                QuidditchRole role = QuidditchRole.valueOf(args[0].toUpperCase());
                roleManager.setPlayerRole(player, role);
                giveBroomstick(player); // Give the player a broomstick upon selecting a role
                player.sendMessage(ChatColor.GREEN + "You have chosen the role of " + role.getDisplayName() + ". Here's your Quidditch broomstick!");
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid role. Please choose a valid Quidditch role.");
            }
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Please specify a role.");
        }
        return false; // False if no valid command was processed
    }

    /**
     * Gives a broomstick to the player, symbolizing their participation in the Quidditch game.
     *
     * @param player The player to receive the broomstick.
     */
    /*private void giveBroomstick(Player player) {
        ItemStack broomstick = new ItemStack(Material.STICK); // Represents the Quidditch broomstick
        ItemMeta meta = broomstick.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Quidditch Broomstick");
            broomstick.setItemMeta(meta);
        }
        player.getInventory().addItem(broomstick); // Add the broomstick to the player's inventory
    }*/
}
