package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for the Quidditch Bukkit plugin.
 * Initializes game components, commands, and event listeners for managing a Quidditch game.
 */
public class QuidditchPlugin extends JavaPlugin {
    private QuidditchGame quidditchGame;
    private RoleManager roleManager;

    @Override
    public void onEnable() {
        // Load configuration files and initialize game and role management
        saveDefaultConfig(); // Saves the default config.yml if one doesn't exist
        reloadConfig(); // Loads the config.yml into memory for use

        // Initialize the core game logic handler
        quidditchGame = new QuidditchGame(this);
        quidditchGame.setup(); // Set up game environment, including teams and scoreboards

        // Initialize the role management system
        roleManager = new RoleManager();

        // Register command executors for game commands
        this.getCommand("jointeam").setExecutor(new TeamCommandExecutor(quidditchGame));
        this.getCommand("leaveteam").setExecutor(new TeamCommandExecutor(quidditchGame));
        // Note: RoleCommandExecutor now correctly receives both roleManager and quidditchGame
        this.getCommand("chooseclass").setExecutor(new RoleCommandExecutor(roleManager, quidditchGame));
        this.getCommand("clearclass").setExecutor(new RoleCommandExecutor(roleManager, quidditchGame));

        // Register command executors for spawning game elements
        this.getCommand("spawnsnitch").setExecutor(new SnitchCommandExecutor(quidditchGame));
        this.getCommand("spawnquaffle").setExecutor(new QuaffleCommandExecutor(this));
        this.getCommand("spawnbludger").setExecutor(new BludgerSpawnCommandExecutor(this));
        this.getCommand("joinquidditch").setExecutor(new QuidditchCommandExecutor(quidditchGame));

        // Register event listeners for in-game interactions
        getServer().getPluginManager().registerEvents(new BeaterInteractionListener(roleManager), this);
        getServer().getPluginManager().registerEvents(new BroomstickFlightListener(), this);
        // Note: QuaffleThrowListener registered twice - potentially by mistake
        getServer().getPluginManager().registerEvents(new QuaffleThrowListener(this, quidditchGame), this);
        getServer().getPluginManager().registerEvents(new QuidditchEventListener(this, roleManager, quidditchGame), this);
        getServer().getPluginManager().registerEvents(new GUIEventListener(quidditchGame), this);
        
        // Setup commands for starting and ending the game with permission checks
        setupGameControlCommands();
    }

    /**
     * Sets up commands for controlling the game state, including permissions checks.
     */
    private void setupGameControlCommands() {
        this.getCommand("startgame").setExecutor((sender, command, label, args) -> {
            if (sender.hasPermission("quidditch.startgame")) {
                quidditchGame.startGame();
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You do not have permission to start the game.");
            return true;
        });

        this.getCommand("endgame").setExecutor((sender, command, label, args) -> {
            if (sender.hasPermission("quidditch.endgame")) {
                if (quidditchGame.isGameRunning()) {
                    quidditchGame.endGame("no clear winner, game ended by admin");
                } else {
                    sender.sendMessage(ChatColor.RED + "There is no game currently running.");
                }
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You do not have permission to end the game.");
            return true;
        });
    }

    @Override
    public void onDisable() {
        // Log a message when the plugin is disabled
        getLogger().info("QuidditchPlugin has been disabled.");
    }
}
