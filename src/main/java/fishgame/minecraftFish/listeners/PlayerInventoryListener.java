package fishgame.minecraftFish.listeners;

import fishgame.minecraftFish.game.GameManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInventoryListener implements Listener {




    private final GameManager gameManager;

    public PlayerInventoryListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;

        event.setCancelled(true);
        gameManager.getMenuManager().processMenuClick(event, player);

        // Need MenuContainer for custom menus

        // Menu handler next
        // Additional menus
        // Buff actions
    }
}