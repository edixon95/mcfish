package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.FishRepository;
import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.player.FishPlayer;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishManager {

    private final List<FishType> fishPool = new ArrayList<>();
    private final FishRepository fishRepository;
    private final Random random = new Random();

    public FishManager(FishRepository fishRepository) {
        this.fishRepository = fishRepository;
        loadFishFromDatabase();
    }

    public FishType rollFish(FishPlayer player) {
        int index = random.nextInt(fishPool.size());
        return fishPool.get(index);
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
                fishPool.add(new FishType("Common Fish", Material.COD, 1, 5));
                fishPool.add(new FishType("Rare Fish", Material.SALMON, 2, 20));
                fishPool.add(new FishType("Golden Fish", Material.TROPICAL_FISH, 3, 100));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}