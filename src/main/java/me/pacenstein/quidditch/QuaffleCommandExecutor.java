package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class QuaffleCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;

    public QuaffleCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Assuming you have a method in your QuidditchGame class to spawn the Quaffle
            if ("spawnquaffle".equalsIgnoreCase(command.getName())) {
                QuidditchGame.spawnQuaffle(player.getLocation());
                player.sendMessage("Quaffle spawned!");
                return true;
            }
        } else {
            sender.sendMessage("This command can only be used by a player.");
        }
        return false;
    }
}
