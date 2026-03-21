package fishgame.minecraftFish.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();


        // Only track clicks in the player's own inventory
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(player.getInventory())) return;

        int slot = event.getSlot(); // 0–8 = hotbar, 9+ = main inventory

        // Only track slots beyond hotbar - TBD
        // if (slot <= 8) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        String componentString = clickedItem.getItemMeta().getAsComponentString();

        Pattern pattern = Pattern.compile("minecraft:custom_data=\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(componentString);

        if (matcher.find()) {
            String data = matcher.group(1);

            String[] entries = data.split(",");
            for (String entry : entries) {
                String[] keyValue = entry.split(":", 2);
                if (keyValue.length < 2) continue;

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                player.sendMessage("Key: " + key + ", Value: " + value);
            }
        } else {
            player.sendMessage("Can't find key", componentString);
        }

        player.sendMessage("You clicked slot " + slot + " containing " + clickedItem.getType());

        event.setCancelled(true);

        // Menu handler next
        // Additional menus
        // Buff actions
    }
}