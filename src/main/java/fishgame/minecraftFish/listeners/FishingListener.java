package fishgame.minecraftFish.listeners;

import fishgame.minecraftFish.game.GameManager;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.player.Upgrade;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.FishHook;
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
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            FishHook hook = event.getHook();

            Upgrade waitTimeMultiplier = fp.getUpgradeById(1);

            int waitTime = Math.max(20, 500 - waitTimeMultiplier.getLevel());
            hook.setMinWaitTime(waitTime);
            hook.setMaxWaitTime(waitTime);

            player.sendMessage("Current wait time: " + waitTime/20);
        }

        if (event.getState() == PlayerFishEvent.State.BITE) {
            event.setCancelled(true);

            int goldWithMultiplier = 1;
            fp.addGold(goldWithMultiplier);
            fp.addFishCaught(1);

            player.sendMessage(Component.text("You caught a fish, value: " + goldWithMultiplier));
        }
    }
}