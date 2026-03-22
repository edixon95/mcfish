package fishgame.minecraftFish.game;

import fishgame.minecraftFish.Misc.Rarity;
import fishgame.minecraftFish.Misc.ServerConfig;
import fishgame.minecraftFish.database.MiscRepository;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.player.Upgrade;
import fishgame.minecraftFish.util.GameUtil;

import java.sql.SQLException;
import java.util.*;

public class MiscManager {

    private final MiscRepository miscRepository;
    private final List<Rarity> rarityPool = new ArrayList<>();

    private final List<Upgrade> upgradePool = new ArrayList<>();
    private final Map<Integer, Upgrade> upgradeIndex = new HashMap<>();

    private final Random random = new Random();
    private final ServerConfig serverConfig;

    public MiscManager(MiscRepository miscRepository) {
        this.miscRepository = miscRepository;

        loadRarityFromDatabase();
        loadUpgradesFromDatabase();
        this.serverConfig = loadServerConfig();
    }

    public Rarity rollRarity(FishPlayer player) {
        return GameUtil.rollScaled(
                rarityPool,
                Rarity::getWeight,
                player.getGradeModifier(), // same idea
                random
        );
    }

    private void loadRarityFromDatabase() {
        try {
            rarityPool.addAll(miscRepository.getAllRarity());

            if (rarityPool.isEmpty()) {
                rarityPool.add(new Rarity("Common", 1, 10));
                rarityPool.add(new Rarity("Uncommon",1.5, 70));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUpgradesFromDatabase() {
        try {
            upgradePool.addAll(miscRepository.getAllUpgrades());

            if (upgradePool.isEmpty()) {
                upgradePool.add(new Upgrade(1, "More Fish", 10, 1, 1));
                upgradePool.add(new Upgrade(2, "Faster Fish", 50, 1, 1));
            }

            for (Upgrade u : upgradePool) {
                upgradeIndex.put(u.getId(), u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Upgrade getUpgradeById(int id) {
        return upgradeIndex.get(id);
    }

    public List<Upgrade> getDefaultUpgrades() {

        List<Upgrade> defaults = new ArrayList<>();

        for (Upgrade base : upgradePool) {

            defaults.add(
                    base.copyWithLevel(1)
            );

        }

        return defaults;
    }

    public String getDefaultPlayerInventory() {
        return serverConfig.getPlayerInventory();
    }

    private ServerConfig loadServerConfig() {
        try {
            ServerConfig cfg = miscRepository.getServerConfig();

            if (cfg == null) {
                throw new RuntimeException("No server config found in database");
            }

            return cfg;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load server config", e);
        }
    }

}
