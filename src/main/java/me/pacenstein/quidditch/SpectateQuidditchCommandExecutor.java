package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class SpectateQuidditchCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;
    private final Location spectateLocation; // Location to spectate from

    public SpectateQuidditchCommandExecutor(QuidditchGame quidditchGame, Location spectateLocation) {
        this.quidditchGame = quidditchGame;
        this.spectateLocation = spectateLocation;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        return handleSpectateCommand(player);
    }

    private boolean handleSpectateCommand(Player player) {
        if (!quidditchGame.isLobbyActive()) {
            player.sendMessage(ChatColor.RED + "There is no active lobby to spectate.");
            return true;
        }

        if (quidditchGame.hasPlayerJoined(player)) {
            player.sendMessage(ChatColor.YELLOW + "You are already participating in the game.");
            return true;
        }
        Location spectateLocation = new Location(Bukkit.getWorld("world"), -83, -20, -11); // Example coordinates

        player.teleport(spectateLocation);
        player.sendMessage(ChatColor.GREEN + "You are now spectating the Quidditch game. Enjoy!");
        return true;
    }
}
