package me.pacenstein.quidditch;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class listens for player interactions with entities within the Quidditch game.
 * It includes logic for handling interactions with the Snitch by seekers.
 */
public class QuidditchEventListener implements Listener {
    private JavaPlugin plugin;
    private RoleManager roleManager;
    private QuidditchGame quidditchGame;

    /**
     * Constructs a new QuidditchEventListener.
     *
     * @param plugin The JavaPlugin instance for the Bukkit plugin.
     * @param roleManager A manager for handling player roles within the game.
     * @param quidditchGame The game instance managing the current state of the Quidditch match.
     */
    public QuidditchEventListener(JavaPlugin plugin, RoleManager roleManager, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.roleManager = roleManager;
        this.quidditchGame = quidditchGame;
    }

    /**
     * Handles player interactions with entities, specifically aiming to catch the Snitch.
     *
     * @param event The event triggered when a player interacts with an entity.
     */
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        // Ensure the game is currently running
        if (!quidditchGame.isGameRunning()) {
            player.sendMessage(ChatColor.RED + "The game has not started yet.");
            return; // Exit if the game hasn't started
        }

        Entity entity = event.getRightClicked();

        // Check if the entity is an ArmorStand tagged as the Snitch
        if (entity instanceof ArmorStand && entity.hasMetadata("Snitch")) {
            QuidditchRole playerRole = roleManager.getPlayerRole(player);

            // Ensure only Seekers can catch the Snitch
            if (playerRole != QuidditchRole.SEEKER) {
                player.sendMessage(ChatColor.RED + "Only Seekers can catch the Snitch!");
                event.setCancelled(true); // Prevent non-seekers from catching the Snitch
            } else {
                // Award points to the seeker's team and determine the game's outcome
                if (quidditchGame.teamA.getEntries().contains(player.getName())) {
                    quidditchGame.scoreTeamA += 150; // Award points to Team A
                } else if (quidditchGame.teamB.getEntries().contains(player.getName())) {
                    quidditchGame.scoreTeamB += 150; // Award points to Team B
                }
                // Determine the winner and end the game
                String winningTeam = quidditchGame.determineWinner();
                quidditchGame.endGame(winningTeam);
            }
        }
    }
}
