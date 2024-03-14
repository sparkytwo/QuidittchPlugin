package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class QuidditchGame {
    private ScoreboardManager manager;
    private Scoreboard scoreboard;
    public Team teamA;
    public Team teamB;

    private JavaPlugin plugin;

    public QuidditchGame(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        scoreboard = manager.getNewScoreboard();

        teamA = scoreboard.registerNewTeam("TeamA");
        teamA.setDisplayName("Team A");
        teamA.setPrefix("A-");

        teamB = scoreboard.registerNewTeam("TeamB");
        teamB.setDisplayName("Team B");
        teamB.setPrefix("B-");
    }

    public void spawnSnitch(Location location) {
        ItemStack snitchItemStack = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = snitchItemStack.getItemMeta();
        meta.setDisplayName("Snitch");
        snitchItemStack.setItemMeta(meta);

        Bukkit.broadcastMessage("The Snitch has been released!");
        spawnFlyingItem(location, snitchItemStack);
    }

    public static void spawnQuaffle(Location location) {
        ItemStack quaffleItemStack = new ItemStack(Material.LEATHER_HELMET); // Assuming a custom item or LEATHER_HELMET for simplicity
        ItemMeta meta = quaffleItemStack.getItemMeta();
        meta.setDisplayName("Quaffle");
        quaffleItemStack.setItemMeta(meta);

        location.getWorld().dropItem(location, quaffleItemStack);
        Bukkit.broadcastMessage("The Quaffle is in play!");
    }

    public void spawnFlyingItem(Location location, final ItemStack itemStack) {
        final Bat bat = location.getWorld().spawn(location, Bat.class);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255));
        bat.setRemoveWhenFarAway(false);

        final ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.getEquipment().setHelmet(itemStack); // Set the snitch as a helmet to make sure it's centered
        armorStand.setCanPickupItems(false);
        armorStand.setRemoveWhenFarAway(false);

        bat.addPassenger(armorStand);

        (new BukkitRunnable() {
            public void run() {
                if (bat.isDead() || armorStand.isDead()) {
                    bat.remove();
                    armorStand.remove();
                    this.cancel();
                }
            }
        }).runTaskTimer(plugin, 20L, 20L);
    }
}