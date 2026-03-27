package fishgame.minecraftFish.listeners;

import fishgame.minecraftFish.game.FishingNPCManager;
import fishgame.minecraftFish.game.GameManager;
import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.player.Upgrade;
import fishgame.minecraftFish.player.UpgradeCalculator;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class FishingListener implements Listener {

    private final GameManager gameManager;
    private final FishingNPCManager fishingNPCManager = new FishingNPCManager();

    public FishingListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    private final Map<UUID, List<FishHook>> extraHooks = new HashMap<>();

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!gameManager.isRunning()) return;

        Player player = event.getPlayer();
        FishPlayer fp = gameManager.getPlayerManager().handleGetPlayer(player.getUniqueId());
        UpgradeCalculator calc = new UpgradeCalculator(fp);


        fp.setFishPower(1);
        if (event.getState() == PlayerFishEvent.State.FISHING) {
            FishHook hook = event.getHook();

            int waitTime = calc.getFishingWaitTimeTicks();
            int lureTime = calc.getFishingSpawnDistanceTimeTicks();

            applyHookSettings(hook, waitTime, lureTime);

            createExtraHooks(player, calc, hook);

            player.sendMessage("Current wait time: " + waitTime/20);
        }

        if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.FAILED_ATTEMPT) {
            player.sendMessage("Attempting to remove extra hooks");
            removeExtraHooks(player);
            fishingNPCManager.removeNPCFromMemory(player);
        }

        if (event.getState() == PlayerFishEvent.State.BITE) {
            event.setCancelled(true);

            int goldWithMultiplier = calc.getFishValue(1);
            fp.addGold(goldWithMultiplier);
            fp.addFishCaught(1);

            player.sendMessage(Component.text("You caught a fish, value: " + goldWithMultiplier));
        }
    }

    private void applyHookSettings(FishHook hook, int waitTime, int lureTime) {

        hook.setMinWaitTime(waitTime);
        hook.setMaxWaitTime(waitTime);

        hook.setMinLureTime(lureTime);
        hook.setMaxLureTime(lureTime);
    }

    private void createExtraHooks(Player player, UpgradeCalculator calc, FishHook ogHook) {
        removeExtraHooks(player);

        int extraHookCount = calc.getFishHooks() - 1;
        List<FishHook> hooks = new ArrayList<>();

        if (extraHookCount >= 1) {
            int waitTime = calc.getFishingWaitTimeTicks();
            int lureTime = calc.getFishingSpawnDistanceTimeTicks();

            for (int i = 0; i < extraHookCount; i++) {
                FishHook hook = player.launchProjectile(FishHook.class);
                Vector forward = player.getLocation().getDirection().normalize();

                Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize();

                int sideIndex = i + 1;
                double offset = (sideIndex - (extraHookCount) / 2.0) * 0.5;

                Vector newDirection = forward.clone().add(right.multiply(offset)).normalize();
                hook.setVelocity(newDirection.multiply(1.2));

                applyHookSettings(hook, waitTime, lureTime);
                hooks.add(hook);
            }
        }
        hooks.add(ogHook);

        FishHook npcHook = addNPCFisher(player, calc);
        hooks.add(npcHook);
        extraHooks.put(player.getUniqueId(), hooks);
    }

    private void removeExtraHooks(Player player) {
        player.sendMessage("Removing hook function start");
        List<FishHook> hooks = extraHooks.remove(player.getUniqueId());
        if (hooks == null) return;

        Bukkit.getScheduler().runTask(gameManager.getPlugin(), () -> {
            for (FishHook hook : hooks) {
                if (hook != null) {
                    player.sendMessage("Removing hooks in a loop");
                    hook.setShooter(null); // detach from player
                    hook.remove();
                }
            }
        });
    }

    private FishHook makeNPCFish(Player realPlayer, Player npcBukkitEntity) {

        FishHook hook = npcBukkitEntity.launchProjectile(FishHook.class);

        // redirect rewards to real player
        hook.setShooter(realPlayer);

        Vector forward = realPlayer.getLocation().getDirection().normalize();

        hook.setVelocity(forward.multiply(1.2));

        return hook;
    }

    private FishHook addNPCFisher(Player player, UpgradeCalculator calc) {
        player.sendMessage("Attempting to spawn NPC");
        NPC npc = fishingNPCManager.spawnFishingNPC(player);
        Player npcAsPlayer = (Player) npc.getEntity();

        FishHook hook = makeNPCFish(player, npcAsPlayer);
        applyHookSettings(
                hook,
                calc.getFishingWaitTimeTicks(),
                calc.getFishingSpawnDistanceTimeTicks()
        );

        return hook;
    }
}