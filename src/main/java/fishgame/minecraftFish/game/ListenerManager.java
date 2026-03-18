package fishgame.minecraftFish.game;

import fishgame.minecraftFish.listeners.FishingListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    private final GameManager gameManager;
    private final JavaPlugin plugin;
    public ListenerManager(JavaPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new FishingListener(gameManager), plugin);
    }
}
