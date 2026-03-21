package fishgame.minecraftFish.game;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fishgame.minecraftFish.Misc.PlayerUpgradeDTO;
import fishgame.minecraftFish.database.PlayerRepository;
import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.player.Upgrade;
import fishgame.minecraftFish.util.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerManager {
    private final PlayerRepository playerRepository;
    private final Map<UUID, FishPlayer> onlinePlayers = new HashMap<>();
    private final JavaPlugin plugin;
    private final MiscManager miscManager;

    public PlayerManager(PlayerRepository playerRepository, JavaPlugin plugin, MiscManager miscManager) {
        this.playerRepository = playerRepository;
        this.plugin = plugin;
        this.miscManager = miscManager;
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

                // Apply upgrades
                String upgradesJSON = result.getString("upgrades");
                applyUpgradesFromJSON(upgradesJSON, fp);

                fp.setGold(result.getInt("currency1"));
                fp.setPremium(result.getInt("currency2"));
            } else {

                playerRepository.createPlayer(uuid, name);

                // give default upgrades (level 1)
                List<Upgrade> defaultUpgrades = miscManager.getDefaultUpgrades();

                fp.setAllUpgrade(defaultUpgrades);

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

    private void applyUpgradesFromJSON(String upgradesJSON, FishPlayer player) {

        if (upgradesJSON == null || upgradesJSON.isEmpty())
            return;

        Gson gson = new Gson();

        Type type = new TypeToken<List<PlayerUpgradeDTO>>(){}.getType();
        List<PlayerUpgradeDTO> storedUpgrades = gson.fromJson(upgradesJSON, type);

        List<Upgrade> playerUpgrades = new ArrayList<>();

        for (PlayerUpgradeDTO dto : storedUpgrades) {

            Upgrade baseUpgrade = miscManager.getUpgradeById(dto.getId());

            if (baseUpgrade != null) {

                Upgrade playerUpgrade =
                        baseUpgrade.copyWithLevel(dto.getLevel());

                playerUpgrades.add(playerUpgrade);
            }
        }

        player.setAllUpgrade(playerUpgrades);
    }


    public void savePlayerAndDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        FishPlayer fp = onlinePlayers.get(uuid);
        if (fp != null) {

            String inventoryJSON = InventorySerializer.inventoryToJSON(player.getInventory());
            String upgradesJSON = upgradesToJSON(fp);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    playerRepository.savePlayerToDatabase(fp, inventoryJSON, upgradesJSON);
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
            String upgradesJSON = upgradesToJSON(fp);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    playerRepository.savePlayerToDatabase(fp, inventoryJSON, upgradesJSON);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private String upgradesToJSON(FishPlayer player) {

        List<PlayerUpgradeDTO> dtoList = new ArrayList<>();

        for (Upgrade u : player.getUpgrades()) {

            dtoList.add(
                    new PlayerUpgradeDTO(
                            u.getId(),
                            u.getLevel()
                    )
            );

        }

        return new Gson().toJson(dtoList);
    }

    private void removePlayer (UUID uuid) {
        onlinePlayers.remove(uuid);
    }

}
