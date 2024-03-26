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

public class BludgerSpawnCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;

    public BludgerSpawnCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("spawnbludger")) {
            Player player = (Player) sender;
            spawnBludger(player);
            sender.sendMessage("Bludger spawned!");
            return true;
        }

        return false;
    }

    private void spawnBludger(Player player) {
        // Spawn the Bat at the player's location to represent the Bludger's movement
        Bat bat = player.getWorld().spawn(player.getLocation(), Bat.class);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false)); // Optional: Make the bat invisible

        // Create an invisible ArmorStand at the bat's location
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false); // Make the ArmorStand invisible
        armorStand.setSmall(true); // Optional: Make the ArmorStand small
        armorStand.setBasePlate(false); // Remove the base plate
        armorStand.setCanPickupItems(false); // Prevent the ArmorStand from picking up items

        // Set a fire charge to be the head or held item
        ItemStack fireChargeItem = new ItemStack(Material.FIRE_CHARGE);
        armorStand.getEquipment().setHelmet(fireChargeItem); // Set the fire charge as the helmet for visual effect

        // Attach the ArmorStand to the Bat
        bat.addPassenger(armorStand);

        // Start the Bludger behavior targeting logic
        new BludgerBehavior(plugin, bat, armorStand).runTaskTimer(plugin, 0L, 20L); // Adjust timing as needed
    }

}
