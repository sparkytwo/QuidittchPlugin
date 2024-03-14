package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommandExecutor implements CommandExecutor {
    private QuidditchGame quidditchGame;

    public TeamCommandExecutor(QuidditchGame game) {
        this.quidditchGame = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("jointeam")) {
            if (args.length > 0) {
                if (quidditchGame.teamA.getEntries().contains(player.getName()) || quidditchGame.teamB.getEntries().contains(player.getName())) {
                    player.sendMessage("You are already on a team. Use /leaveteam to leave your current team first.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("A")) {
                    quidditchGame.teamA.addEntry(player.getName());
                    player.sendMessage("You have joined Team A.");
                } else if (args[0].equalsIgnoreCase("B")) {
                    quidditchGame.teamB.addEntry(player.getName());
                    player.sendMessage("You have joined Team B.");
                } else {
                    player.sendMessage("Invalid team. Please choose A or B.");
                }
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("leaveteam")) {
            if (quidditchGame.teamA.getEntries().contains(player.getName())) {
                quidditchGame.teamA.removeEntry(player.getName());
                player.sendMessage("You have left Team A.");
            } else if (quidditchGame.teamB.getEntries().contains(player.getName())) {
                quidditchGame.teamB.removeEntry(player.getName());
                player.sendMessage("You have left Team B.");
            } else {
                player.sendMessage("You are not currently on any team.");
            }
            return true;
        }
        return false;
    }
}
