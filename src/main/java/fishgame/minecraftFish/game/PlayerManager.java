package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.PlayerRepository;
import fishgame.minecraftFish.fish.FishType;
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

    public FishPlayer handleGetPlayer(UUID uuid) {
        return onlinePlayers.get(uuid);
    }

    public boolean handleRarityModifier(String name, double rarityModifier) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {return false;}
        UUID uuid = player.getUniqueId();

        FishPlayer fp = handleGetPlayer(uuid);
        fp.setRarityModifier(rarityModifier);

        return true;
    }

    public boolean handleGradeModifier(String name, double gradeModifier) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {return false;}
        UUID uuid = player.getUniqueId();

        FishPlayer fp = handleGetPlayer(uuid);
        fp.setGradeModifier(gradeModifier);

        return true;
    }

    // Player connections
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
                fp.setFishCaught(result.getInt("fish_caught"));

                // Apply inventory
                String inventoryJSON = result.getString("inventory");
                applyInventoryFromJSON(inventoryJSON, player);

                fp.setGold(result.getInt("currency1"));
                fp.setPremium(result.getInt("currency2"));
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

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    playerRepository.savePlayerToDatabase(fp, inventoryJSON);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            removePlayer(uuid);
        }
    }

    public void autoSaveAllPlayers() {
        for (Map.Entry<UUID, FishPlayer> entry : onlinePlayers.entrySet()) {
            UUID uuid = entry.getKey();
            FishPlayer fp = entry.getValue();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            String inventoryJSON = InventorySerializer.inventoryToJSON(player.getInventory());

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    playerRepository.savePlayerToDatabase(fp, inventoryJSON);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void removePlayer (UUID uuid) {
        onlinePlayers.remove(uuid);
    }

}
