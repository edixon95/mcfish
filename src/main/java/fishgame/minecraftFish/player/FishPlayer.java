package fishgame.minecraftFish.player;

import java.util.UUID;

public class FishPlayer {

    private final UUID uuid;
    private final String name;
    private int fishCaught;
    private int gold;
    private int premium;
    private int catchInt;

    public FishPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.fishCaught = 0;
        this.gold = 0;
        this.premium = 0;
        this.catchInt = 70;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getFishCaught() {
        return fishCaught;
    }

    public int getGold() {
        return gold;
    }

    public int getPremium() {
        return premium;
    }

    public void setFishCaught(int fishCaught) {
        this.fishCaught = fishCaught;
    }

    public void addFishCaught(int amount) {
        this.fishCaught += amount;
    }

    public void setGold(int amount) {
        this.gold = amount;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void addPremium(int amount) {
        this.premium += amount;
    }

    public void setPremium(int amount) {
        this.premium = amount;
    }
}