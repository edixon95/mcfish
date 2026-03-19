package fishgame.minecraftFish.player;

import java.util.UUID;

public class FishPlayer {

    private final UUID uuid;
    private final String name;
    private int fishCaught;
    private int gold;
    private int premium;
    private double rarityModifier;
    private int fishPower;

    public FishPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.fishCaught = 0;
        this.gold = 0;
        this.premium = 0;
        this.rarityModifier = 1;
        this.fishPower = 0;
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

    public int getFishPower() {
        return fishPower;
    }

    public double getRarityModifier() { return rarityModifier;}

    public int getGold() {
        return gold;
    }

    public int getPremium() {
        return premium;
    }

    public void setFishPower(int power) {
        this.fishPower = power;
    }

    public void setFishCaught(int fishCaught) {
        this.fishCaught = fishCaught;
    }

    public void addFishCaught(int amount) {
        this.fishCaught += amount;
    }

    public void setRarityModifier(double amount) {this.rarityModifier = amount;}

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