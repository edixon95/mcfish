package fishgame.minecraftFish.player;

public class UpgradeCalculator {

    private final FishPlayer player;

    public UpgradeCalculator(FishPlayer player) {
        this.player = player;
    }

    private int getLevel(int upgradeId) {
        Upgrade upgrade = player.getUpgradeById(upgradeId);
        return upgrade != null ? upgrade.getLevel() : 0;
    }

    public int getFishingWaitTimeTicks() {

        int level = getLevel(UpgradeIds.WAIT_TIME);

        int baseTicks = 500;

        int reduction = level * 15;

        return Math.max(20, baseTicks - reduction);
    }

    public int getFishingSpawnDistanceTimeTicks() {

        int baseTicks = 80;
        int level = getLevel(UpgradeIds.CLOSER_SPAWN);
        int reduction = level * 5;

        return Math.max(10, baseTicks - reduction);
    }

    public int getFishValue(int base) {

        int level = getLevel(UpgradeIds.MORE_MONEY);

        return level * base;
    }

    public int getFishHooks() {
        return getLevel(UpgradeIds.MORE_HOOKS);
    }
}