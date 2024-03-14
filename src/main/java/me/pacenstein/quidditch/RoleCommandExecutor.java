package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (command.getName().equalsIgnoreCase("chooseclass")) {
            if (args.length > 0) {
                try {
                    QuidditchRole role = QuidditchRole.valueOf(args[0].toUpperCase());
                    roleManager.setPlayerRole(player, role);
                } catch (IllegalArgumentException e) {
                    player.sendMessage("Invalid class. Please choose from Guardian, Hunter, Seeker, or Driver.");
                }
                return true;
            } else {
                player.sendMessage("Please specify a class.");
            }
        }
        return false;
    }
}
