package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.PlayerRepository;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.util.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerManager {
    private final PlayerRepository playerRepository;
    private final Map<UUID, FishPlayer> onlinePlayers = new HashMap<>();
    private final JavaPlugin plugin;

    public PlayerManager(PlayerRepository playerRepository, JavaPlugin plugin) {
        this.playerRepository = playerRepository;
        this.plugin = plugin;
    }

    public void loadPlayerOnJoin (PlayerJoinEvent event) {
        loadPlayerFromDatabaseAndApplyInventory(event.getPlayer());
    }

    private void loadPlayerFromDatabaseAndApplyInventory(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();
        FishPlayer fp = new FishPlayer(uuid, name);

        try {
            ResultSet result = playerRepository.getPlayerFromDatabase(uuid);
            if (result.next()) {
                // Set stats here that get copied over
                fp.setFishCaught(result.getInt("fish_caught"));
                // Apply inventory
                String inventoryJSON = result.getString("inventory");
                applyInventoryFromJSON(inventoryJSON, player);
            } else {
                playerRepository.createPlayer(uuid, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        handlePlayerJoin(fp);
    }

    private void handlePlayerJoin(FishPlayer player) {
        onlinePlayers.put(player.getUuid(), player);
    }

    private void applyInventoryFromJSON(String inventoryJSON, Player player) {
        if (inventoryJSON == null || inventoryJSON.isEmpty()) return;

        try {
            ItemStack[] items = InventorySerializer.inventoryFromJSON(inventoryJSON);

            // Schedule on main thread since Bukkit API is not thread-safe
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.getInventory().setContents(items);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayerAndDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        FishPlayer fp = onlinePlayers.get(uuid);
        if (fp != null) {

            String inventoryJSON = InventorySerializer.inventoryToJSON(player.getInventory());

            try {
                playerRepository.savePlayerToDatabase(fp, inventoryJSON);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            removePlayer(uuid);
        }
    }

    private void removePlayer (UUID uuid) {
        onlinePlayers.remove(uuid);
    }

}
