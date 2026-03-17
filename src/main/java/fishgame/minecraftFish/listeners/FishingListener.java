//package fishgame.minecraftFish.listeners;
//
//import fishgame.minecraftFish.fish.FishType;
//import fishgame.minecraftFish.game.GameManager;
//import fishgame.minecraftFish.game.FishInventoryManager;
//import net.kyori.adventure.text.Component;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerFishEvent;
//
//import java.util.Random;
//
//public class FishingListener implements Listener {
//
//    private final GameManager gameManager;
//    private final FishInventoryManager inventoryManager;
//    private final Random random = new Random();
//
//    public FishingListener(GameManager gameManager, FishInventoryManager inventoryManager) {
//        this.gameManager = gameManager;
//        this.inventoryManager = inventoryManager;
//    }
//
//    @EventHandler
//    public void onFish(PlayerFishEvent event) {
//        if (!gameManager.isRunning()) return;
//
//        Player player = event.getPlayer();
//
//        if (event.getState() == PlayerFishEvent.State.BITE) {
//            event.setCancelled(true);
//
//            // Roll a fish
//            FishType caught = gameManager.rollFish();
//
//            // Add to the custom inventory
//            inventoryManager.addFish(player, caught);
//
//            player.sendMessage(Component.text("You caught a " + caught.getName() + "!"));
//        }
//    }
//}