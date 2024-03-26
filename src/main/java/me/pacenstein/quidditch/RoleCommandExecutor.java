package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RoleCommandExecutor implements CommandExecutor {
    private final RoleManager roleManager;
    private final QuidditchGame quidditchGame;

    public RoleCommandExecutor(RoleManager roleManager, QuidditchGame game) {
        this.roleManager = roleManager;
        this.quidditchGame = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length > 0) {
            try {
                QuidditchRole role = QuidditchRole.valueOf(args[0].toUpperCase());
                roleManager.setPlayerRole(player, role);
                giveBroomstick(player); // Give the player a broomstick upon role selection
                player.sendMessage("You have chosen the role of " + role.getDisplayName() + " and received your Quidditch broomstick.");
            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid role. Please choose a valid Quidditch role.");
            }
            return true;
        } else {
            player.sendMessage("Please specify a role.");
        }
        return false;
    }

    private void giveBroomstick(Player player) {
        ItemStack broomstick = new ItemStack(Material.STICK); // Or whatever item you choose
        ItemMeta meta = broomstick.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Quidditch Broomstick");
            broomstick.setItemMeta(meta);
        }
        player.getInventory().addItem(broomstick);
        // Optionally set some properties or flags indicating the player is in the game and has a broomstick
    }
}