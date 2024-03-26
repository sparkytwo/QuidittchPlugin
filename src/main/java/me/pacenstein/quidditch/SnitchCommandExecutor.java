package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command executor for spawning the Snitch in the Quidditch game.
 * This class listens for the "spawnsnitch" command and spawns the Snitch at the command sender's location.
 */
public class SnitchCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    /**
     * Constructs a SnitchCommandExecutor with a reference to the main Quidditch game logic.
     *
     * @param game The Quidditch game instance where the Snitch should be spawned.
     */
    public SnitchCommandExecutor(QuidditchGame game) {
        this.quidditchGame = game;
    }

    /**
     * Handles the "spawnsnitch" command, spawning the Snitch at the player's location.
     *
     * @param sender  The sender of the command. This must be a player.
     * @param command The command that was executed.
     * @param label   The alias of the command that was used.
     * @param args    The command arguments.
     * @return true if the command was processed correctly, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure that the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true because the command syntax is correct
        }

        // Process the "spawnsnitch" command
        if (command.getName().equalsIgnoreCase("spawnsnitch")) {
            Player player = (Player) sender;
            // Spawn the Snitch at the player's current location
            quidditchGame.spawnSnitch(player.getLocation());
            sender.sendMessage("The Snitch has been spawned!");
            return true; // Command executed successfully
        }

        // If the command is not recognized (though this should not happen due to Bukkit's command handling), return false
        return false;
    }
}
