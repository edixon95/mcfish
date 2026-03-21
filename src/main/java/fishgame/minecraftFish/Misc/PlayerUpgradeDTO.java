package fishgame.minecraftFish.Misc;

public class PlayerUpgradeDTO {

    private final int id;
    private final int level;

    public PlayerUpgradeDTO(int id, int level) {
        this.id = id;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
