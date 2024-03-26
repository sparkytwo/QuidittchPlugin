package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Command executor for handling Quaffle-related commands in a Quidditch game plugin.
 *
 * This class listens for specific commands input by players and performs actions related
 * to the Quaffle, such as spawning it at the player's location.
 */
public class QuaffleCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;

    /**
     * Constructs a new QuaffleCommandExecutor.
     *
     * @param plugin The JavaPlugin instance for access to plugin-wide methods and properties.
     */
    public QuaffleCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     *
     * @param sender  The source of the command.
     * @param command The Command being executed.
     * @param label   The alias of the command that was used.
     * @param args    The command arguments.
     * @return true if the command was valid and successfully executed, otherwise false.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player, as this command requires player context
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Execute the spawnQuaffle action if the "spawnquaffle" command is issued
            if ("spawnquaffle".equalsIgnoreCase(command.getName())) {
                // Here, it's assumed QuidditchGame class has a static method to spawn the Quaffle
                // This action spawns the Quaffle at the player's current location
                QuidditchGame.spawnQuaffle(player.getLocation());
                player.sendMessage("Quaffle spawned!"); // Notify the player
                return true; // Indicate successful command execution
            }
        } else {
            // Inform non-player command senders they can't use this command
            sender.sendMessage("This command can only be used by a player.");
        }
        return false; // Return false for any conditions not meeting command requirements
    }
}
