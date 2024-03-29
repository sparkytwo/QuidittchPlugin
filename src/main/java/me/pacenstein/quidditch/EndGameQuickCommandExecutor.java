package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndGameQuickCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public EndGameQuickCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("quidditch.endgamequick")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to end the game.");
            return true;
        }

        // End the game, clear the lobby, and remove all game-related entities/items
        quidditchGame.endGamePrematurely();
        sender.sendMessage(ChatColor.GREEN + "The Quidditch game has been ended prematurely.");
        return true;
    }
}
