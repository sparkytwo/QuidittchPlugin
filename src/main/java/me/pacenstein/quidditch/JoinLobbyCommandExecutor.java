package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class JoinLobbyCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public JoinLobbyCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        return handlePlayerCommand((Player) sender);
    }

    private boolean handlePlayerCommand(Player player) {
        if (quidditchGame.isGameRunning()) {
            player.sendMessage(ChatColor.RED + "A game is already in progress. Please wait until it finishes.");
            return true;
        }

        quidditchGame.addPlayerToLobby(player);
        player.sendMessage(ChatColor.GREEN + "You have joined the Quidditch lobby!");
        return true;
    }
}
