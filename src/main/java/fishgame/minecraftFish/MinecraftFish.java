package fishgame.minecraftFish;

import fishgame.minecraftFish.database.Database;
import fishgame.minecraftFish.game.GameManager;
import fishgame.minecraftFish.game.ListenerManager;
import fishgame.minecraftFish.player.FishPlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fishgame.minecraftFish.util.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.UUID;

public final class MinecraftFish extends JavaPlugin implements Listener {

    private Database database;
    private GameManager gameManager;
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        database = new Database();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Could not connect to database!");
            return;
        }

        // Initialize game manager with database reference
        gameManager = new GameManager(this, database);
        listenerManager = new ListenerManager(this, gameManager);

        listenerManager.registerListeners();

        // Register events -- DONT REMOVE THIS
        getServer().getPluginManager().registerEvents(this, this);

        // Register command executor
        this.getCommand("fishgame").setExecutor(new fishgame.minecraftFish.commands.FishGameCommand(gameManager));

        Bukkit.getScheduler().runTaskTimer(
                this,
                () -> gameManager.getPlayerManager().autoSaveAllPlayers(),
                20L * 30,
                20L * 60
        );

        getLogger().info("MinecraftFish plugin started");
    }

    @Override
    public void onDisable() {
        // Save all online players
//        for (FishPlayer player : gameManager.getOnlinePlayers()) {
//            gameManager.savePlayer(player);
//        }
//
//        try {
//            database.disconnect();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
        getLogger().info("MinecraftFish plugin stopped");
    }

    @org.bukkit.event.EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        gameManager.getPlayerManager().loadPlayerOnJoin(event);
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        gameManager.getPlayerManager().savePlayerAndDisconnect(event);
    }

    public GameManager getGameManager() {
        return gameManager;
    }
    public ListenerManager getListenerManager() { return listenerManager;}
}