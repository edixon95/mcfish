package fishgame.minecraftFish.player;

public class Upgrade {

    private final int id;
    private final String name;
    private final int baseCost;
    private final double scale;
    private int level;

    public Upgrade (int id, String name, int baseCost, double scale, int level) {
        this.id = id;
        this.name = name;
        this.baseCost = baseCost;
        this.scale = scale;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public double getScale() { return scale;}

    public int getLevel() {
        return level;
    }

    public Upgrade copyWithLevel(int level) {
        return new Upgrade(id, name, baseCost, scale, level);
    }

    public int getScaledPrice() {
        return (int) (baseCost * level * scale);
    }

    public void increaseLevel(int amount) {
        this.level += amount;
    }
}
