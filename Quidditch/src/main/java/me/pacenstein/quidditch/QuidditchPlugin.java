package me.pacenstein.quidditch;

import org.bukkit.plugin.java.JavaPlugin;

public class QuidditchPlugin extends JavaPlugin {
    private QuidditchGame quidditchGame;
    private RoleManager roleManager;


    @Override
    public void onEnable() {
        quidditchGame = new QuidditchGame(this);
        quidditchGame.setup();

        roleManager = new RoleManager();

        // Update to pass both roleManager and quidditchGame
        this.getCommand("jointeam").setExecutor(new TeamCommandExecutor(quidditchGame));
        this.getCommand("leaveteam").setExecutor(new TeamCommandExecutor(quidditchGame));
        this.getCommand("chooseclass").setExecutor(new RoleCommandExecutor(roleManager, quidditchGame)); // Modified
        this.getCommand("spawnsnitch").setExecutor(new SnitchCommandExecutor(quidditchGame));
        this.getCommand("spawnquaffle").setExecutor(new QuaffleCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new QuidditchEventListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("QuidditchPlugin has been disabled.");
    }
}
