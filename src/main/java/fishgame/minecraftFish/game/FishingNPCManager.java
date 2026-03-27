package fishgame.minecraftFish.game;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FishingNPCManager {
    private final Map<UUID, NPC> fishingNpcs = new HashMap<>();

    public NPC spawnFishingNPC(Player player) {
        Location pLocation = player.getLocation();
        Location npcLocation = pLocation.clone().add(1.5, 0, 0);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(
                EntityType.PLAYER,
                player.getName() + "'s buddy"
        );

        npc.spawn(npcLocation);

        addNpcToMemory(npc, player);

        return npc;
    }

    private void addNpcToMemory(NPC npc, Player player) {
        fishingNpcs.put(player.getUniqueId(), npc);
    }

    public void removeNPCFromMemory(Player player) {
        NPC npc = fishingNpcs.remove(player.getUniqueId());

        if (npc == null) return;

        if (npc.isSpawned()) {
            npc.despawn();
        }

        npc.destroy();
    }
}
