package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public LeaveGameCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        // Remove player from the lobby and any team they might be on
        quidditchGame.removePlayerFromLobby(player);
        quidditchGame.removePlayerFromTeam(player);
        // Clear Quidditch-related items
        quidditchGame.clearPlayerQuidditchItems(player);

        player.sendMessage(ChatColor.GREEN + "You have left the Quidditch game.");
        return true;
    }
}
