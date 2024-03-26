package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Executes commands for joining and leaving teams in the Quidditch game.
 * Ensures that team changes are not allowed once the game has started.
 */
public class TeamCommandExecutor implements CommandExecutor {
    private QuidditchGame quidditchGame;

    /**
     * Constructs a new TeamCommandExecutor with a reference to the Quidditch game.
     *
     * @param game The Quidditch game instance.
     */
    public TeamCommandExecutor(QuidditchGame game) {
        this.quidditchGame = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verify the game is not currently running before processing the command
        if (quidditchGame.isGameRunning()) {
            sender.sendMessage(ChatColor.RED + "You cannot join or leave teams once the game has started.");
            return true;
        }

        // Ensure the command is executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Handle the "jointeam" command
        if (command.getName().equalsIgnoreCase("jointeam")) {
            if (args.length > 0) {
                // Prevent players from joining a new team if they are already on one
                if (quidditchGame.teamA.getEntries().contains(player.getName()) || quidditchGame.teamB.getEntries().contains(player.getName())) {
                    player.sendMessage(ChatColor.RED + "You are already on a team. Use /leaveteam to leave your current team first.");
                    return true;
                }

                // Join Team A
                if (args[0].equalsIgnoreCase("A")) {
                    quidditchGame.teamA.addEntry(player.getName());
                    player.sendMessage(ChatColor.GREEN + "You have joined Team A.");
                }
                // Join Team B
                else if (args[0].equalsIgnoreCase("B")) {
                    quidditchGame.teamB.addEntry(player.getName());
                    player.sendMessage(ChatColor.GREEN + "You have joined Team B.");
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid team. Please choose A or B.");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Please specify a team to join (A or B).");
            }
        }
        // Handle the "leaveteam" command
        else if (command.getName().equalsIgnoreCase("leaveteam")) {
            // Leave Team A
            if (quidditchGame.teamA.getEntries().contains(player.getName())) {
                quidditchGame.teamA.removeEntry(player.getName());
                player.sendMessage(ChatColor.YELLOW + "You have left Team A.");
            }
            // Leave Team B
            else if (quidditchGame.teamB.getEntries().contains(player.getName())) {
                quidditchGame.teamB.removeEntry(player.getName());
                player.sendMessage(ChatColor.YELLOW + "You have left Team B.");
            } else {
                player.sendMessage(ChatColor.RED + "You are not currently on any team.");
            }
            return true;
        }

        // If the command is neither "jointeam" nor "leaveteam"
        return false;
    }
}
