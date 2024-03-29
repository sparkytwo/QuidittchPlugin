package me.pacenstein.quidditch;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuidditchGame {
    private Map<Player, QuidditchRole> playerRoles = new HashMap<>();
    private List<Player> lobbyPlayers = new ArrayList<>();
    public List<Player> getLobbyPlayers() {
        return new ArrayList<>(lobbyPlayers); // Assuming lobbyPlayers is a List<Player>
    }

    public String getTeamForPlayer(Player player) {
        if (teamA.hasEntry(player.getName())) return "Team A";
        if (teamB.hasEntry(player.getName())) return "Team B";
        return null; }



    private boolean gameRunning = false;
    private ScoreboardManager manager;
    private Scoreboard scoreboard;
    public Team teamA;
    public Team teamB;

    int scoreTeamA = 0;
    int scoreTeamB = 0;

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



    public void endGame(String winningTeam) {
        gameRunning = false;
        if ("no clear winner, game ended by admin".equals(winningTeam)) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "The Quidditch game has ended without a clear winner.");
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "The Quidditch game has ended. Congratulations to " + winningTeam + "!");
        }
        resetGameEnvironment();
        resetScores();
        // Additional cleanup as necessary
    }

    private void setupTeams() {
        // Logic to lock teams and finalize player roles
    }

    private void spawnInitialElements() {
        // Logic to spawn the Snitch, Quaffles, and Bludgers
    }

    private void resetGameEnvironment() {
        // Logic to reset the game environment, including player positions and inventories
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void scoreGoal(String team) {
        if ("TeamA".equals(team)) {
            scoreTeamA += 10;
            Bukkit.broadcastMessage("Team A scores! Current score: " + scoreTeamA + "-" + scoreTeamB);
        } else if ("TeamB".equals(team)) {
            scoreTeamB += 10;
            Bukkit.broadcastMessage("Team B scores! Current score: " + scoreTeamA + "-" + scoreTeamB);
        }
        updateScoreboard();
    }



    public String determineWinner() {
        if (scoreTeamA > scoreTeamB) return "Team A";
        else if (scoreTeamB > scoreTeamA) return "Team B";
        else return "It's a tie!";
    }



    private void updateScoreboard() {
        // Logic to update the visible scoreboard with current scores
    }

    private void resetScores() {
        // Reset team scores and update the scoreboard accordingly
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
        armorStand.setMetadata("Snitch", new FixedMetadataValue(plugin, true));


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
                            if (isInGoal(location, "TeamA")) {
                                entity.remove(); // Remove the Quaffle from the world
                                scoreGoal("TeamA");
                                Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "Team A scores!");
                            } else if (isInGoal(location, "TeamB")) {
                                entity.remove(); // Remove the Quaffle from the world
                                scoreGoal("TeamB");
                                Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "Team B scores!");
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Run this check every second (20 ticks)
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

    public void assignPlayerToTeam(Player player, String teamName) {
        if (teamName.equals("TeamA")) {
            if (!teamA.hasEntry(player.getName())) {
                teamB.removeEntry(player.getName()); // Remove from Team B if present
                teamA.addEntry(player.getName());
                player.sendMessage(ChatColor.GREEN + "You have joined Team A.");
            }
        } else if (teamName.equals("TeamB")) {
            if (!teamB.hasEntry(player.getName())) {
                teamA.removeEntry(player.getName()); // Remove from Team A if present
                teamB.addEntry(player.getName());
                player.sendMessage(ChatColor.GREEN + "You have joined Team B.");
            }
        }
    }


    public void assignPlayerClass(Player player, String className) {
        QuidditchRole role;
        try {
            role = QuidditchRole.valueOf(className.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid class name. Please choose a valid role.");
            return;
        }

        // Assign the role to the player
        playerRoles.put(player, role);
        player.sendMessage(ChatColor.GREEN + "You are now a " + role.name() + ".");

        // Example logic to give role-specific items or effects
        switch (role) {
            case CHASER:
                // Give Chaser items/effects
                break;
            case BEATER:
                // Give Beater items/effects
                break;
            case KEEPER:
                // Give Keeper items/effects
                break;
            case SEEKER:
                // Give Seeker items/effects
                break;
        }
    }


    public void openSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Select Team and Class");

        // Team A selection
        ItemStack teamAItem = new ItemStack(Material.RED_WOOL);
        ItemMeta teamAMeta = teamAItem.getItemMeta();
        if (teamAMeta != null) {
            teamAMeta.setDisplayName(ChatColor.RED + "Join Team A");
            teamAItem.setItemMeta(teamAMeta);
        }

        // Team B selection
        ItemStack teamBItem = new ItemStack(Material.BLUE_WOOL);
        ItemMeta teamBMeta = teamBItem.getItemMeta();
        if (teamBMeta != null) {
            teamBMeta.setDisplayName(ChatColor.BLUE + "Join Team B");
            teamBItem.setItemMeta(teamBMeta);
        }

        // Chaser class selection
        ItemStack chaserItem = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta chaserMeta = chaserItem.getItemMeta();
        if (chaserMeta != null) {
            chaserMeta.setDisplayName(ChatColor.GOLD + "Choose Chaser");
            chaserItem.setItemMeta(chaserMeta);
        }

        // Beater class selection
        ItemStack beaterItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta beaterMeta = beaterItem.getItemMeta();
        if (beaterMeta != null) {
            beaterMeta.setDisplayName(ChatColor.GRAY + "Choose Beater");
            beaterItem.setItemMeta(beaterMeta);
        }

        // Keeper class selection
        ItemStack keeperItem = new ItemStack(Material.SHIELD);
        ItemMeta keeperMeta = keeperItem.getItemMeta();
        if (keeperMeta != null) {
            keeperMeta.setDisplayName(ChatColor.GREEN + "Choose Keeper");
            keeperItem.setItemMeta(keeperMeta);
        }

        // Seeker class selection
        ItemStack seekerItem = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta seekerMeta = seekerItem.getItemMeta();
        if (seekerMeta != null) {
            seekerMeta.setDisplayName(ChatColor.YELLOW + "Choose Seeker");
            seekerItem.setItemMeta(seekerMeta);
        }

        // Setting items in the GUI
        gui.setItem(10, teamAItem);
        gui.setItem(12, teamBItem);
        gui.setItem(14, chaserItem);
        gui.setItem(15, beaterItem);
        gui.setItem(16, keeperItem);
        gui.setItem(17, seekerItem);

        player.openInventory(gui);
    }

    public void openTeamSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, "Select Team");

        // Team A selection
        ItemStack teamAItem = new ItemStack(Material.RED_WOOL);
        ItemMeta teamAMeta = teamAItem.getItemMeta();
        if (teamAMeta != null) {
            teamAMeta.setDisplayName(ChatColor.RED + "Join Team A");
            teamAItem.setItemMeta(teamAMeta);
        }

        // Team B selection
        ItemStack teamBItem = new ItemStack(Material.BLUE_WOOL);
        ItemMeta teamBMeta = teamBItem.getItemMeta();
        if (teamBMeta != null) {
            teamBMeta.setDisplayName(ChatColor.BLUE + "Join Team B");
            teamBItem.setItemMeta(teamBMeta);
        }

        // Setting items in the GUI
        gui.setItem(3, teamAItem);
        gui.setItem(5, teamBItem);

        player.openInventory(gui);
    }

    public void openClassSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Select Class");

        // Chaser class selection
        ItemStack chaserItem = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta chaserMeta = chaserItem.getItemMeta();
        if (chaserMeta != null) {
            chaserMeta.setDisplayName(ChatColor.GOLD + "Choose Chaser");
            chaserItem.setItemMeta(chaserMeta);
        }

        // Beater class selection
        ItemStack beaterItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta beaterMeta = beaterItem.getItemMeta();
        if (beaterMeta != null) {
            beaterMeta.setDisplayName(ChatColor.GRAY + "Choose Beater");
            beaterItem.setItemMeta(beaterMeta);
        }

        // Keeper class selection
        ItemStack keeperItem = new ItemStack(Material.SHIELD);
        ItemMeta keeperMeta = keeperItem.getItemMeta();
        if (keeperMeta != null) {
            keeperMeta.setDisplayName(ChatColor.GREEN + "Choose Keeper");
            keeperItem.setItemMeta(keeperMeta);
        }

        // Seeker class selection
        ItemStack seekerItem = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta seekerMeta = seekerItem.getItemMeta();
        if (seekerMeta != null) {
            seekerMeta.setDisplayName(ChatColor.YELLOW + "Choose Seeker");
            seekerItem.setItemMeta(seekerMeta);
        }

        // Setting items in the GUI for selection
        gui.setItem(10, chaserItem);
        gui.setItem(12, beaterItem);
        gui.setItem(14, keeperItem);
        gui.setItem(16, seekerItem);

        // Open the inventory GUI for the player
        player.openInventory(gui);
    }

    public void addPlayerToLobby(Player player) {
        if (!lobbyPlayers.contains(player)) { // Check if the player is not already in the lobby
            // Teleport player to the lobby area (you need to define the lobby location)
            lobbyPlayers.add(player);
            player.sendMessage(ChatColor.GREEN + "Welcome to the Quidditch lobby! Waiting for more players...");
            // Consider adding a check here to start a lobby countdown if enough players have joined
        } else {
            player.sendMessage(ChatColor.YELLOW + "You are already in the lobby.");
        }
    }

    public void removePlayerFromLobby(Player player) {
        lobbyPlayers.remove(player);
    }

    public void startLobbyCountdown() {
        new BukkitRunnable() {
            int countdown = 30; // 30 seconds countdown

            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Game starts in " + countdown + " seconds!");
                    countdown--;
                } else {
                    startGame(); // Method to start the game, assign teams, teleport players, etc.
                    this.cancel(); // Stop the countdown
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20L = 1 second in ticks
    }

    public void startGame() {
        // Check if there are enough players in the lobby to start the game.
        // For testing purposes, we're considering 1 player as enough to start.
        if (lobbyPlayers.size() < 1) {
            Bukkit.broadcastMessage(ChatColor.RED + "Can't start the game with an empty queue.");
            return; // Exit the method if not enough players are in the lobby.
        }

        if (gameRunning) {
            Bukkit.broadcastMessage(ChatColor.RED + "A game is already in progress.");
            return; // Exit the method if a game is already running.
        }

        // Assuming you have logic to assign players to teams and setup the game
        // For simplicity, the example below assigns the first player to Team A and starts the game.
        Player player = lobbyPlayers.get(0); // Get the first player in the lobby
        assignPlayerToTeam(player, "TeamA"); // Assign the player to Team A for testing

        // Teleport the player to the game area, setup the game environment, etc.
        // This is a simplified example, you'll need to expand this based on your game's requirements.

        gameRunning = true; // Set the game state to running
        Bukkit.broadcastMessage(ChatColor.GREEN + "The Quidditch game has started with " + lobbyPlayers.size() + " players.");

        // Clear the lobby for the next game
        lobbyPlayers.clear();
    }

    public void removePlayerFromTeam(Player player) {
        if (teamA.hasEntry(player.getName())) {
            teamA.removeEntry(player.getName());
        } else if (teamB.hasEntry(player.getName())) {
            teamB.removeEntry(player.getName());
        }
    }

    public void clearPlayerQuidditchItems(Player player) {
        // Example: Clear specific items. Adjust according to your game's items
        player.getInventory().remove(Material.GOLD_NUGGET); // Assuming GOLD_NUGGET is the Snitch
        player.getInventory().remove(Material.LEATHER_HELMET); // Assuming LEATHER_HELMET is the Quaffle
        // Add removal logic for any other custom items you use
    }

    public void endGamePrematurely() {
        // End the game, clear the lobby and any game-specific setups
        gameRunning = false;
        lobbyPlayers.clear();
        Bukkit.broadcastMessage(ChatColor.GOLD + "The Quidditch game has been ended prematurely.");

        // Reset scores or any other game state as needed
        scoreTeamA = 0;
        scoreTeamB = 0;
        updateScoreboard(); // Assuming you have a method to update the scoreboard

        // Remove game-related entities. This might include removing spawned Snitches, Quaffles, etc.
        // This is an example and needs to be tailored to how you spawn and manage these entities.
        Bukkit.getWorlds().forEach(world -> world.getEntitiesByClasses(ArmorStand.class, Item.class).forEach(entity -> {
            if (entity.hasMetadata("Snitch") || entity.hasMetadata("Quaffle")) {
                entity.remove();
            }
        }));
    }

    public boolean hasPlayerJoined(Player player) {
        // Check if player is in the lobby or has been assigned a team
        return lobbyPlayers.contains(player) || teamA.hasEntry(player.getName()) || teamB.hasEntry(player.getName());
    }




   /*   public void startGame() {
        if (!gameRunning) {
            gameRunning = true;
            Bukkit.broadcastMessage(ChatColor.GREEN + "The Quidditch game has started!");
            // Lock team modifications and perform any necessary setup.
            setupTeams();
            spawnInitialElements(); // Spawn Snitch, Quaffles, and Bludgers
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "A game is already in progress.");
        }
    }*/


}


