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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

public class QuaffleGoalListener implements Listener {
    private JavaPlugin plugin;
    private QuidditchGame quidditchGame; // Reference to your QuidditchGame instance

    public QuaffleGoalListener(JavaPlugin plugin, QuidditchGame quidditchGame) {
        this.plugin = plugin;
        this.quidditchGame = quidditchGame;
    }

    @EventHandler
    public void onPlayerMoveWithQuaffle(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (isCarryingQuaffle(player)) {
            if (quidditchGame.isInGoal(player.getLocation(), "TeamA") || quidditchGame.isInGoal(player.getLocation(), "TeamB")) {
                String scoringTeam = quidditchGame.getTeamForPlayer(player);
                plugin.getServer().broadcastMessage(player.getName() + " scores for " + scoringTeam + "!");

                // Remove Quaffle from scoring player
                player.getInventory().removeItem(getQuaffle());

                // Find and give Quaffle to the nearest keeper of the scoring team
                Player nearestKeeper = findNearestKeeper(player.getLocation(), scoringTeam);
                if (nearestKeeper != null) {
                    giveQuaffleToPlayer(nearestKeeper); // Use the new method to give the Quaffle
                    plugin.getServer().broadcastMessage(nearestKeeper.getName() + " is now holding the Quaffle!");
                }
            }
        }
    }

    private Player findNearestKeeper(Location location, String teamName) {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        Player nearestKeeper = players.stream()
                .filter(isKeeperOfTeam(teamName))
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)))
                .orElse(null);

        // Broadcast the nearest keeper's name, if found
        if (nearestKeeper != null) {
            plugin.getServer().broadcastMessage("Nearest keeper to the goal is: " + nearestKeeper.getName());
        } else {
            plugin.getServer().broadcastMessage("No keeper found for team " + teamName);
        }

        return nearestKeeper;
    }

    private Predicate<Player> isKeeperOfTeam(String teamName) {
        return player -> quidditchGame.getTeamForPlayer(player).equals(teamName) &&
                quidditchGame.getPlayerRoles().get(player) == QuidditchRole.KEEPER;
    }

    private void giveQuaffleToPlayer(Player player) {
        ItemStack quaffle = getQuaffle();
        HashMap<Integer, ItemStack> noSpace = player.getInventory().addItem(quaffle); // Attempt to add the item

        if (!noSpace.isEmpty()) { // Check if inventory was full
            player.getWorld().dropItemNaturally(player.getLocation(), quaffle); // Drop the Quaffle at the player's location
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
