package fishgame.minecraftFish.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class GameUtil {

    public static <T> T weightedPick(Collection<T> items, Function<T, Double> weightFunction, Random random) {
        double totalWeight = 0;
        Map<T, Double> weights = new HashMap<>();

        for (T item : items) {
            double weight = weightFunction.apply(item);

            if (weight <= 0) continue;

            weights.put(item, weight);
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            return null;
        }

        double roll = random.nextDouble() * totalWeight;

        for (Map.Entry<T, Double> entry : weights.entrySet()) {
            roll -= entry.getValue();
            if (roll <= 0) {
                return entry.getKey();
            }
        }

        return weights.keySet().stream().findFirst().orElse(null);
    }
}