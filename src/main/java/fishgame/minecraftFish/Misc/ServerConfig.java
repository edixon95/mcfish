package fishgame.minecraftFish.Misc;

public class ServerConfig {

    private final int id;
    private final double version;
    private final String playerInventory;

    public ServerConfig(int id, double version, String playerInventory) {
        this.id = id;
        this.version = version;
        this.playerInventory = playerInventory;
    }

    public int getId() {
        return id;
    }

    public double getVersion() {
        return version;
    }

    public String getPlayerInventory() {
        return playerInventory;
    }
}
