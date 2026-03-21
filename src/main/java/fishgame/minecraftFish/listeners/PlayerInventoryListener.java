package fishgame.minecraftFish.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        // Only track clicks in the player's own inventory
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(player.getInventory())) return;

        int slot = event.getRawSlot(); // 0–8 = hotbar, 9+ = main inventory

        // Only track slots beyond hotbar
        if (slot <= 8) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // Track the click
        player.sendMessage("You clicked slot " + slot + " containing " + clickedItem.getType());

        // Here you can call your game logic:
        // gameManager.getPlayerManager().handleInventoryClick(player, slot, clickedItem);
    }
}