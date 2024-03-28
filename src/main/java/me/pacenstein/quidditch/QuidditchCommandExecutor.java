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
            return true;
        }

        if (command.getName().equalsIgnoreCase("joinquidditch")) {
            Player player = (Player) sender;
            // Now calling openTeamSelectionGUI instead of openSelectionGUI
            quidditchGame.openTeamSelectionGUI(player);
            return true;
        }

        return false;
    }
}