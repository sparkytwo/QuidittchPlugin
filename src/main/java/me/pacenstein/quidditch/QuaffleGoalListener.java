package me.pacenstein.quidditch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Comparator;
import java.util.Collection;
import java.util.function.Predicate;

public class QuaffleGoalListener implements Listener {
    private JavaPlugin plugin;
    private QuidditchGame quidditchGame;

    public QuaffleGoalListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerMoveWithQuaffle(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!isCarryingQuaffle(player)) {
            return;
        }

        Location location = player.getLocation();
        if (!quidditchGame.isInGoal(location, "TeamA") && !quidditchGame.isInGoal(location, "TeamB")) {
            return;
        }

        String scoringTeam = quidditchGame.getTeamForPlayer(player);
        plugin.getServer().broadcastMessage(player.getName() + " scores for " + scoringTeam + "!");

        player.getInventory().removeItem(getQuaffle());

        Player nearestKeeper = findNearestKeeper(location, scoringTeam);
        if (nearestKeeper != null) {
            giveQuaffleToPlayer(nearestKeeper);
            plugin.getServer().broadcastMessage(nearestKeeper.getName() + " is now holding the Quaffle!");
        }
    }

    private Player findNearestKeeper(Location location, String teamName) {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        return players.stream()
                .filter(isKeeperOfTeam(teamName))
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)))
                .orElse(null);
    }

    private Predicate<Player> isKeeperOfTeam(String teamName) {
        return player -> quidditchGame.getTeamForPlayer(player).equals(teamName) &&
                quidditchGame.getPlayerRoles().get(player) == QuidditchRole.KEEPER;
    }

    private void giveQuaffleToPlayer(Player player) {
        ItemStack quaffle = getQuaffle();
        if (!player.getInventory().addItem(quaffle).isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), quaffle);
            plugin.getServer().broadcastMessage("Dropped the Quaffle at " + player.getName() + "'s location because the inventory was full.");
        }
    }

    private ItemStack getQuaffle() {
        ItemStack quaffle = new ItemStack(Material.LEATHER);
        ItemMeta meta = quaffle.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Quaffle");
            quaffle.setItemMeta(meta);
        }
        return quaffle;
    }

    private boolean isCarryingQuaffle(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        return itemInHand != null && itemInHand.hasItemMeta() &&
                "Quaffle".equals(itemInHand.getItemMeta().getDisplayName());
    }
}
