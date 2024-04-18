package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuidditchCommandExecutor implements CommandExecutor {

    private QuidditchGame quidditchGame;

    public QuidditchCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;  // Return true to indicate the syntax is correct but execution isn't allowed for non-players
        }

        return handlePlayerCommand((Player) sender, command);
    }

    private boolean handlePlayerCommand(Player player, Command command) {
        if ("joinquidditch".equalsIgnoreCase(command.getName())) {
            quidditchGame.openTeamSelectionGUI(player);
            player.sendMessage("Welcome to Quidditch! Please select your team.");
            return true;
        }
        return false;  // Return false if the command is not recognized
    }
}
