package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.FishRepository;
import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.util.GameUtil;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.*;

public class FishManager {

    private final List<FishType> fishPool = new ArrayList<>();
    private final FishRepository fishRepository;
    private final Random random = new Random();

    public FishManager(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
        loadFishFromDatabase();
    }

    public FishType rollFish(FishPlayer player) {
        List<FishType> eligibleFish = fishPool.stream()
                .filter(fish -> fish.getFishPower() <= player.getFishPower())
                .toList();

        if (eligibleFish.isEmpty()) {
            return null;
        }

        return GameUtil.rollScaled(
                eligibleFish,
                FishType::getRarityWeight,
                player.getRarityModifier(), // now acts as "power"
                random
        );
    }

    public void reloadFish() {
        fishPool.clear();
        loadFishFromDatabase();
    }

    public FishType[] getFishPool() {
        return fishPool.toArray(new FishType[0]);
    }

    private void loadFishFromDatabase() {
        try {
            fishPool.addAll(fishRepository.getAllFish());

            if (fishPool.isEmpty()) {
                fishPool.add(new FishType("Common Fish", Material.COD, 1, 5, 1));
                fishPool.add(new FishType("Rare Fish", Material.SALMON, 2, 20, 1));
                fishPool.add(new FishType("Golden Fish", Material.TROPICAL_FISH, 3, 100, 2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}