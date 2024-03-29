package me.pacenstein.quidditch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShowLobbyCommandExecutor implements CommandExecutor {
    private final QuidditchGame quidditchGame;

    public ShowLobbyCommandExecutor(QuidditchGame quidditchGame) {
        this.quidditchGame = quidditchGame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        openLobbyGUI(player);
        return true;
    }

    private void openLobbyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "Lobby & Teams");

        // For each player in the lobby, create an item stack representing them
        for (Player lobbyPlayer : quidditchGame.getLobbyPlayers()) {
            ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD); // Use player head or any other appropriate item
            ItemMeta meta = playerItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + lobbyPlayer.getName());

                List<String> lore = new ArrayList<>();
                String teamName = quidditchGame.getTeamForPlayer(lobbyPlayer);
                lore.add(ChatColor.GRAY + "Team: " + (teamName != null ? teamName : "Not assigned"));
                meta.setLore(lore);

                playerItem.setItemMeta(meta);
            }
            gui.addItem(playerItem);
        }

        player.openInventory(gui);
    }
}
