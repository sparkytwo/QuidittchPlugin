package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class JoinQuidditchCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public JoinQuidditchCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        return handleJoinCommand(player);
    }

    private boolean handleJoinCommand(Player player) {
        if (quidditchGame.isGameRunning()) {
            player.sendMessage(ChatColor.RED + "A game is already in progress. Please wait until it finishes.");
            return true;
        }

        if (quidditchGame.hasPlayerJoined(player)) {
            player.sendMessage(ChatColor.YELLOW + "You have already joined the Quidditch game.");
            return true;
        }

        quidditchGame.addPlayerToLobby(player);
        quidditchGame.openTeamSelectionGUI(player);
        player.sendMessage(ChatColor.GREEN + "You have joined the Quidditch game and can now select a team.");
        return true;
    }
}
