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

    private static final NamespacedKey actionKey = new NamespacedKey("minecraftfish", "action");
    private static final NamespacedKey targetKey = new NamespacedKey("minecraftfish", "target");
    private static final NamespacedKey upgradeKey = new NamespacedKey("minecraftfish", "upgrade_id");


    private final GameManager gameManager;

    public PlayerInventoryListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        // Only track clicks in the player's own inventory
        if (event.getClickedInventory() == null) return;

        int slot = event.getSlot(); // 0–8 = hotbar, 9+ = main inventory

        // Hotbar belongs to the player
        if (event.getClickedInventory().equals(player.getInventory()) && slot <= 8) { return;}
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        handleMenuClick(meta, player);



        // Need MenuContainer for custom menus

        // Menu handler next
        // Additional menus
        // Buff actions
    }

    private void handleMenuClick(ItemMeta meta, Player player) {
        PersistentDataContainer data = meta.getPersistentDataContainer();
        String action = data.get(actionKey, PersistentDataType.STRING);
        if (action == null) return;

        switch (action) {
            case "navigate":
                String target = data.get(targetKey, PersistentDataType.STRING);
                if (target == null) return;
                player.sendMessage("Clicked to navigate to " + target);
                gameManager.getMenuManager().handleMenuClick(target, player);
                break;

            case "purchase_upgrade":
                Integer upgradeVal = data.get(upgradeKey, PersistentDataType.INTEGER);
                if (upgradeVal == null) return; // Null check then treat as int for class simplicity
                int upgrade = upgradeVal;
                player.sendMessage("Clicked to upgrade " + upgrade);
                break;

            default:
                player.sendMessage("Action Type: \"" + action + "\" does not have a function");
                break;
        }

    }
}