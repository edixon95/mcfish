package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.Database;
import fishgame.minecraftFish.database.FishRepository;
import fishgame.minecraftFish.database.PlayerRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class GameManager {
    private boolean running = false;
    public void startGame() {
        running = true;
    }
    public void stopGame() {
        running = false;
    }
    public boolean isRunning() {
        return running;
    }

    private final FishManager fishManager;
    private final PlayerManager playerManager;

    public GameManager(JavaPlugin plugin, Database database) {
        FishRepository fishRepo = new FishRepository(database);
        PlayerRepository playerRepo = new PlayerRepository(database);

        this.fishManager = new FishManager(fishRepo);
        this.playerManager = new PlayerManager(playerRepo, plugin);
    }

    public FishManager getFishManager() {
        return fishManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}