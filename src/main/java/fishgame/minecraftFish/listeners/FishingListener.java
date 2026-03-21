package fishgame.minecraftFish.listeners;

import fishgame.minecraftFish.Misc.Rarity;
import fishgame.minecraftFish.fish.FishType;
import fishgame.minecraftFish.game.GameManager;
import fishgame.minecraftFish.player.FishPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {

    private final GameManager gameManager;

    public FishingListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!gameManager.isRunning()) return;

        Player player = event.getPlayer();
        FishPlayer fp = gameManager.getPlayerManager().handleGetPlayer(player.getUniqueId());

        fp.setFishPower(1);

        if (event.getState() == PlayerFishEvent.State.BITE) {
            event.setCancelled(true);

//            FishType caught = gameManager.getFishManager().rollFish(fp);
//            Rarity grade = gameManager.getMiscManager().rollRarity(fp);

            int goldWithMultiplier = 1;
            fp.addGold(goldWithMultiplier);
            fp.addFishCaught(1);

            player.sendMessage(Component.text("You caught a fish, value: " + goldWithMultiplier));
        }
    }
}