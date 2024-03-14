package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnitchCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public SnitchCommandExecutor(QuidditchGame game) {
        this.quidditchGame = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("spawnsnitch")) {
            Player player = (Player) sender;
            quidditchGame.spawnSnitch(player.getLocation());
            sender.sendMessage("The Snitch has been spawned!");
            return true;
        }
        return false;
    }
}
