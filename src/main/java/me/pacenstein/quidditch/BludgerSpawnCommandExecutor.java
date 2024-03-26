package me.pacenstein.quidditch;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * This class handles the execution of a command that allows players to spawn a Bludger.
 */
public class BludgerSpawnCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;

    /**
     * Constructor to set the plugin instance.
     *
     * @param plugin The plugin instance.
     */
    public BludgerSpawnCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command was executed successfully, otherwise false.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true; // Return true to indicate that the command syntax is correct
        }

        // Process the "spawnbludger" command
        if (command.getName().equalsIgnoreCase("spawnbludger")) {
            Player player = (Player) sender;
            spawnBludger(player); // Call method to spawn the Bludger
            sender.sendMessage("Bludger spawned!");
            return true; // Command executed successfully
        }

        return false; // Command not recognized or not executed
    }

    /**
     * Spawns a Bludger at the player's location.
     *
     * @param player The player at whose location the Bludger will be spawned.
     */
    private void spawnBludger(Player player) {
        // Spawn a Bat to represent the Bludger
        Bat bat = player.getWorld().spawn(player.getLocation(), Bat.class);
        // Make the Bat invisible for aesthetic purposes
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        // Spawn an ArmorStand for visual effects
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setGravity(false); // Prevent it from falling
        armorStand.setVisible(false); // Make it invisible
        armorStand.setSmall(true); // Optionally make it small
        armorStand.setBasePlate(false); // Remove the base plate for aesthetics
        armorStand.setCanPickupItems(false); // Prevent item pickup

        // Visual effect: Set a fire charge as the head of the ArmorStand
        ItemStack fireChargeItem = new ItemStack(Material.FIRE_CHARGE);
        armorStand.getEquipment().setHelmet(fireChargeItem);

        // Attach the ArmorStand to the Bat to move together
        bat.addPassenger(armorStand);

        // Initialize and start the Bludger behavior task
        new BludgerBehavior(plugin, bat, armorStand).runTaskTimer(plugin, 0L, 20L); // Run task with a period of 20 ticks
    }
}
