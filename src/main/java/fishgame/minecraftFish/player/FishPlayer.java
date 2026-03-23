package fishgame.minecraftFish.player;

import fishgame.minecraftFish.fish.FishType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FishPlayer {

    private final UUID uuid;
    private final String name;
    private int fishCaught;
    private int gold;
    private int premium;
    private double rarityModifier;
    private int fishPower;
    private double gradeModifier;
    private final List<Upgrade> upgrades = new ArrayList<>();

    public FishPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.fishCaught = 0;
        this.gold = 0;
        this.premium = 0;
        this.rarityModifier = 1;
        this.fishPower = 0;
        this.gradeModifier = 0;
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

    public double getGradeModifier() {return gradeModifier;}

    public Upgrade[] getUpgrades() {
        return upgrades.toArray(new Upgrade[0]);
    }

    public Upgrade getUpgradeById(int id) {
        for (Upgrade upgrade : upgrades) {
            if (upgrade.getId() == id) {
                return upgrade;
            }
        }
        return null;
    }

    public void setAllUpgrade(List<Upgrade> upgradesToSet) {
        upgrades.addAll(upgradesToSet);
    }

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

    public void setGradeModifier(double amount) {this.gradeModifier = amount;}

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