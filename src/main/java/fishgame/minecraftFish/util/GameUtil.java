package fishgame.minecraftFish.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.function.ToIntFunction;

public class GameUtil {

    public static <T> T rollScaled(
            Collection<T> items,
            ToIntFunction<T> weightFunction,
            double playerPower,
            Random random
    ) {
        if (items == null || items.isEmpty()) {
            return null;
        }

        double roll = rollPlayerPower(playerPower, random);

        T best = null;
        int bestWeight = Integer.MIN_VALUE;

        for (T item : items) {
            int weight = weightFunction.applyAsInt(item);

            // If player roll can "reach" this rarity
            if (roll >= weight && weight > bestWeight) {
                best = item;
                bestWeight = weight;
            }
        }

        if (best == null) {
            return items.stream()
                    .min(Comparator.comparingInt(weightFunction))
                    .orElse(null);
        }

        return best;
    }

    private static double rollPlayerPower(double playerPower, Random random) {
        double roll = Math.pow(random.nextDouble(), 0.7);
        return roll * playerPower;
    }
}