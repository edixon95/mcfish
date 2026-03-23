package fishgame.minecraftFish.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class DynamicMenu {

    private Inventory createDynamicMenu(MenuItem menu) {
        Inventory inventory = Bukkit.createInventory(
                null,
                menu.getSize(),
                menu.getName()
        );

        inventory.setContents(menu.getMenuInventoryAsItemStack());

        return inventory;
    }

    public void handleGetMenu(MenuItem menu, Player player) {
        Inventory menuInv = createDynamicMenu(menu);
        player.openInventory(menuInv);
    }
}
