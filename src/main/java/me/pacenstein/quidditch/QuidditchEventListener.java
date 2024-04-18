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
 * This class listens for player interactions with entities within the Quidditch game,
 * specifically handling interactions with the Snitch by seekers.
 */
public class QuidditchEventListener implements Listener {
    private JavaPlugin plugin;
    private RoleManager roleManager;
    private QuidditchGame quidditchGame;

    public QuidditchEventListener(JavaPlugin plugin, RoleManager roleManager, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.roleManager = roleManager;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!quidditchGame.isGameRunning()) {
            player.sendMessage(ChatColor.RED + "The game has not started yet.");
            return;
        }

        if (isSnitch(event.getRightClicked())) {
            handleSnitchInteraction(player, event);
        }
    }

    private boolean isSnitch(Entity entity) {
        return entity instanceof ArmorStand && entity.hasMetadata("Snitch");
    }

    private void handleSnitchInteraction(Player player, PlayerInteractAtEntityEvent event) {
        if (roleManager.getPlayerRole(player) != QuidditchRole.SEEKER) {
            player.sendMessage(ChatColor.RED + "Only Seekers can catch the Snitch!");
            event.setCancelled(true);
            return;
        }

        awardPointsToPlayerTeam(player);
        concludeGame();
    }

    private void awardPointsToPlayerTeam(Player player) {
        if (quidditchGame.teamA.getEntries().contains(player.getName())) {
            quidditchGame.scoreTeamA += 150; // Award points to Team A
        } else if (quidditchGame.teamB.getEntries().contains(player.getName())) {
            quidditchGame.scoreTeamB += 150; // Award points to Team B
        }
    }

    private void concludeGame() {
        String winningTeam = quidditchGame.determineWinner();
        quidditchGame.endGame(winningTeam);
    }
}
