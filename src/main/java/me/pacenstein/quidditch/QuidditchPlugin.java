package me.pacenstein.quidditch;

import org.bukkit.plugin.java.JavaPlugin;

public class QuidditchPlugin extends JavaPlugin {
    private QuidditchGame quidditchGame;
    private RoleManager roleManager;


    @Override
    public void onEnable() {
        saveDefaultConfig(); // Ensures config.yml is copied out of your jar
        reloadConfig(); // Loads config.yml into memory
        quidditchGame = new QuidditchGame(this);
        quidditchGame.setup();

        roleManager = new RoleManager();

        // Update to pass both roleManager and quidditchGame
        this.getCommand("jointeam").setExecutor(new TeamCommandExecutor(quidditchGame));
        this.getCommand("leaveteam").setExecutor(new TeamCommandExecutor(quidditchGame));
        this.getCommand("chooseclass").setExecutor(new RoleCommandExecutor(roleManager, quidditchGame)); // Modified
        this.getCommand("spawnsnitch").setExecutor(new SnitchCommandExecutor(quidditchGame));
        this.getCommand("spawnquaffle").setExecutor(new QuaffleCommandExecutor(this));
        this.getCommand("spawnbludger").setExecutor(new BludgerSpawnCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new QuidditchEventListener(this), this);
        getServer().getPluginManager().registerEvents(new QuaffleGoalListener(this, quidditchGame), this);
        getServer().getPluginManager().registerEvents(new BeaterInteractionListener(roleManager), this);
        getServer().getPluginManager().registerEvents(new BeaterInteractionListener(new RoleManager()), this);
        getServer().getPluginManager().registerEvents(new BroomstickFlightListener(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("QuidditchPlugin has been disabled.");
    }
}
