package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for the Quidditch Bukkit plugin.
 * Initializes game components, commands, and event listeners for managing a Quidditch game.
 */
public class QuidditchPlugin extends JavaPlugin {
    private QuidditchGame quidditchGame;
    private RoleManager roleManager;

    Location spectateLocation = new Location(Bukkit.getWorld("world"), -83, -20, -11); // Example coordinates


    @Override
    public void onEnable() {
        initializeConfig();
        initializeGameComponents();
        registerCommands();
        registerEventListeners();
        setupGameControlCommands();
    }

    private void initializeConfig() {
        saveDefaultConfig(); // Saves the default config.yml if one doesn't exist
        reloadConfig(); // Loads the config.yml into memory for use
    }

    private void initializeGameComponents() {
        quidditchGame = new QuidditchGame(this);
        quidditchGame.setup(); // Set up game environment, including teams and scoreboards
        roleManager = new RoleManager();
        quidditchGame.setRoleManager(roleManager); // Set the role manager to the game
    }

    private void registerCommands() {
        getCommand("jointeam").setExecutor(new TeamCommandExecutor(quidditchGame));
        getCommand("leaveteam").setExecutor(new TeamCommandExecutor(quidditchGame));
        getCommand("spawnsnitch").setExecutor(new SnitchCommandExecutor(quidditchGame));
        getCommand("spawnquaffle").setExecutor(new QuaffleCommandExecutor(this));
        getCommand("spawnbludger").setExecutor(new BludgerSpawnCommandExecutor(this));
        getCommand("joinquidditch").setExecutor(new JoinQuidditchCommandExecutor(quidditchGame));
        getCommand("leavegame").setExecutor(new LeaveGameCommandExecutor(quidditchGame));
        getCommand("showlobby").setExecutor(new ShowLobbyCommandExecutor(quidditchGame));
        getCommand("spectatequidditch").setExecutor(new SpectateQuidditchCommandExecutor(quidditchGame, spectateLocation));

    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new BeaterInteractionListener(roleManager), this);
        getServer().getPluginManager().registerEvents(new BroomstickFlightListener(), this);
        getServer().getPluginManager().registerEvents(new QuaffleThrowListener(this, quidditchGame), this);
        getServer().getPluginManager().registerEvents(new QuidditchEventListener(this, roleManager, quidditchGame), this);
        getServer().getPluginManager().registerEvents(new GUIEventListener(quidditchGame), this);
        getServer().getPluginManager().registerEvents(new PlayerHitListener(this, quidditchGame), this);

    }

    /**
     * Sets up commands for controlling the game state, including permissions checks.
     */
    private void setupGameControlCommands() {
        getCommand("startgame").setExecutor((sender, command, label, args) -> {
            if (!sender.hasPermission("quidditch.startgame")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to start the game.");
                return true;
            }
            quidditchGame.startGame();
            return true;
        });

        getCommand("endgame").setExecutor((sender, command, label, args) -> {
            if (!sender.hasPermission("quidditch.endgame")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to end the game.");
                return true;
            }
            if (!quidditchGame.isGameRunning()) {
                sender.sendMessage(ChatColor.RED + "There is no game currently running.");
                return true;
            }
            quidditchGame.endGame("no clear winner, game ended by admin");
            return true;
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("QuidditchPlugin has been disabled.");
    }
}
