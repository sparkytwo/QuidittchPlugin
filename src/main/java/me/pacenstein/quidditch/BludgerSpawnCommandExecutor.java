package me.pacenstein.quidditch;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * This class handles the execution of a command that allows players to spawn a Bludger.
 */
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

        if ("spawnbludger".equalsIgnoreCase(command.getName())) {
            spawnBludger((Player) sender);
            sender.sendMessage("Bludger spawned!");
            return true;
        }

        return false;
    }

    private void spawnBludger(Player player) {
        Bat bat = player.getWorld().spawn(player.getLocation(), Bat.class);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        bat.setMetadata("Bludger", new FixedMetadataValue(plugin, true));

        ArmorStand armorStand = createVisualEffect(player);
        armorStand.setMetadata("Bludger", new FixedMetadataValue(plugin, true));

        bat.addPassenger(armorStand);

        new BludgerBehavior(plugin, bat).runTaskTimer(plugin, 0L, 20L);
    }

    private ArmorStand createVisualEffect(Player player) {
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomName("Bludger");
        armorStand.setCustomNameVisible(true);
        armorStand.setMetadata("Bludger", new FixedMetadataValue(plugin, true));


        ItemStack fireChargeItem = new ItemStack(Material.LEATHER_HORSE_ARMOR);
        armorStand.getEquipment().setHelmet(fireChargeItem);
        return armorStand;
    }
}
