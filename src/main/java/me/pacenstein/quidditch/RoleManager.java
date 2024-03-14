package me.pacenstein.quidditch;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class RoleManager {
    private final Map<Player, QuidditchRole> playerRoles;

    public RoleManager() {
        playerRoles = new HashMap<>();
    }

    public void setPlayerRole(Player player, QuidditchRole role) {
        playerRoles.put(player, role);
        // Here you can also apply any abilities or effects specific to each role
        player.sendMessage("You are now a " + role.getDisplayName());
    }

    public QuidditchRole getPlayerRole(Player player) {
        return playerRoles.getOrDefault(player, null);
    }

    public void clearPlayerRole(Player player) {
        playerRoles.remove(player);
        player.sendMessage("Your Quidditch role has been cleared.");
    }
}
