package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
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

    private Location[] teamAGoals = new Location[3];
    private Location[] teamBGoals = new Location[3];
    private int radius = 4; // Radius of the goals

    private JavaPlugin plugin;

    public QuidditchGame(JavaPlugin plugin) {

        this.plugin = plugin;
        startQuaffleTracking(); // Start tracking thrown Quaffles

        // Initialize team A's goals
        teamAGoals[0] = new Location(Bukkit.getWorld("world"), -36, -9, -77); // Adjust Y value as needed
        teamAGoals[1] = new Location(Bukkit.getWorld("world"), -25, -1, -77); // Adjust coordinates as needed
        teamAGoals[2] = new Location(Bukkit.getWorld("world"), -14, -5, -77); // Adjust coordinates as needed

        // Initialize team B's goals
        teamBGoals[0] = new Location(Bukkit.getWorld("world"), -36, -6, 57); // Adjust Y value as needed
        teamBGoals[1] = new Location(Bukkit.getWorld("world"), -26, -1, 57); // Adjust coordinates as needed
        teamBGoals[2] = new Location(Bukkit.getWorld("world"), -14, -9, 57); // Adjust coordinates as needed

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

    public void startQuaffleTracking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item && entity.hasMetadata("Quaffle")) {
                            Location location = entity.getLocation();
                            if (isInGoal(location, "teamA") || isInGoal(location, "teamB")) {
                                entity.remove(); // Remove the Quaffle from the world
                                // Announce goal and which team scored if needed
                                Bukkit.getServer().broadcastMessage("A goal was scored!");
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Adjust the second parameter for the period
    }

    public boolean isInGoal(Location location, String team) {
        // Arrays to hold the center locations of the goals for teams A and B
        Location[] centersA = new Location[3];
        Location[] centersB = new Location[3];
        int radius = 5; // Assuming a radius of 3 for the goals

        // Populate the arrays with actual center points of your goals
        centersA[0] = new Location(location.getWorld(), -36, -9, -77);
        centersA[1] = new Location(location.getWorld(), -25, -1, -77);
        centersA[2] = new Location(location.getWorld(), -14, -5, -77);
        centersB[0] = new Location(location.getWorld(), -36, -6, 57);
        centersB[1] = new Location(location.getWorld(), -26, -1, 57);
        centersB[2] = new Location(location.getWorld(), -14, -9, 57);

        // Determine which team's goals to check based on the input parameter
        Location[] teamGoals = "teamA".equalsIgnoreCase(team) ? centersA : "teamB".equalsIgnoreCase(team) ? centersB : null;

        // If the team is not recognized, print an error and return false
        if (teamGoals == null) {
            System.out.println("[QuidditchPlugin] Error: Team not recognized.");
            return false;
        }

        // Check if the location is within the radius of any of the team's goals
        for (Location center : teamGoals) {
            if (center.distance(location) <= radius) {
                return true; // The location is within the radius of one of the team's goals
            }
        }

        return false; // The location is not within the radius of any of the team's goals
    }



}


