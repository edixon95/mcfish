package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.*;
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
    private final MiscManager miscManager;
    private final MenuManager menuManager;

    public GameManager(JavaPlugin plugin, Database database) {
        FishRepository fishRepo = new FishRepository(database);
        PlayerRepository playerRepo = new PlayerRepository(database);
        MiscRepository miscRepo = new MiscRepository(database);
        MenuRepository menuRepo = new MenuRepository(database);

        this.fishManager = new FishManager(fishRepo);
        this.miscManager = new MiscManager(miscRepo);
        this.playerManager = new PlayerManager(playerRepo, plugin, miscManager);
        this.menuManager = new MenuManager(menuRepo, this);

    }

    public FishManager getFishManager() {
        return fishManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public MiscManager getMiscManager() { return miscManager;}
    public MenuManager getMenuManager() {return menuManager;}
}