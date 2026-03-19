package fishgame.minecraftFish.Misc;

public class Rarity {

    private final String name;
    private final double multiplier;
    private final int weight;

    public Rarity(String name, double multiplier, int weight) {
        this.name = name;
        this.multiplier = multiplier;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getWeight() {
        return weight;
    }
}
