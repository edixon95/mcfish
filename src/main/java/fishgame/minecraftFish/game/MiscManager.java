package fishgame.minecraftFish.game;

import fishgame.minecraftFish.Misc.Rarity;
import fishgame.minecraftFish.database.FishRepository;
import fishgame.minecraftFish.database.MiscRepository;
import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.util.GameUtil;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiscManager {

    private final MiscRepository miscRepository;
    private final List<Rarity> rarityPool = new ArrayList<>();
    private final Random random = new Random();

    public MiscManager(MiscRepository miscRepository) {
        this.miscRepository = miscRepository;
        loadRarityFromDatabase();
    }

    public Rarity rollRarity(FishPlayer player) {

        double gradeModifier = player.getGradeModifier();

        return GameUtil.weightedPick(
                rarityPool,
                rarity -> {
                    int w = rarity.getWeight();
                    double baseChance = 1.0 / (1.0 + w);
                    return Math.pow(baseChance, 1.0 / (1.0 + gradeModifier));
                },
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
}
