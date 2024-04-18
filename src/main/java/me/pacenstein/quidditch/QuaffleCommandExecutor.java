package me.pacenstein.quidditch;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Command executor for handling Quaffle-related commands in a Quidditch game plugin.
 *
 * This class listens for the "spawnquaffle" command input by players and performs actions
 * related to spawning the Quaffle at the player's location.
 */
public class QuaffleCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;

    /**
     * Constructs a new QuaffleCommandExecutor with a reference to the plugin instance.
     *
     * @param plugin The JavaPlugin instance for access to plugin-wide methods and properties.
     */
    public QuaffleCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true; // Return true to indicate the command syntax is correct but execution isn't allowed
        }

        return handleQuaffleCommand((Player) sender, command);
    }

    private boolean handleQuaffleCommand(Player player, Command command) {
        if ("spawnquaffle".equalsIgnoreCase(command.getName())) {
            QuidditchGame.spawnQuaffle(player.getLocation());
            player.sendMessage("Quaffle spawned at your location!");
            return true;
        }
        return false; // Return false if the command is not recognized
    }
}
