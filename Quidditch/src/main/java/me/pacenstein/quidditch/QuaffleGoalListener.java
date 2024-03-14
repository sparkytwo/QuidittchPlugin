package me.pacenstein.quidditch;

import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.math.BlockVector3;


public class QuaffleGoalListener implements Listener {
    private JavaPlugin plugin;

    public QuaffleGoalListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoveWithQuaffle(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        // Check if the player is carrying the Quaffle
        if (isCarryingQuaffle(player)) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get((World) location.getWorld());
            if (regionManager == null) return; // No regions defined in this world

            // Convert Location to BlockVector3 for WorldGuard region checks
            BlockVector3 vector = BlockVector3.at(location.getX(), location.getY(), location.getZ());
            ApplicableRegionSet set = regionManager.getApplicableRegions(vector);

            for (ProtectedRegion region : set) {
                if (region.getId().equalsIgnoreCase("quidditch_goal_teamA")) {
                    // Handle scoring for Team A
                    plugin.getServer().broadcastMessage(player.getName() + " scores for Team A!");
                    // Reset Quaffle position or other game logic
                } else if (region.getId().equalsIgnoreCase("quidditch_goal_teamB")) {
                    // Handle scoring for Team B
                    plugin.getServer().broadcastMessage(player.getName() + " scores for Team B!");
                    // Reset Quaffle position or other game logic
                }
            }
        }
    }


    private boolean isCarryingQuaffle(Player player) {
        // Implement logic to check if the player is currently carrying the Quaffle
        return false; // Placeholder for your implementation
    }
}
