package fishgame.minecraftFish.player;

import java.util.UUID;

public class FishPlayer {

    private final UUID uuid;
    private final String name;
    private int fishCaught;

    public FishPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.fishCaught = 0;
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

    public void setFishCaught(int fishCaught) {
        this.fishCaught = fishCaught;
    }

    public void addFishCaught(int amount) {
        this.fishCaught += amount;
    }
}