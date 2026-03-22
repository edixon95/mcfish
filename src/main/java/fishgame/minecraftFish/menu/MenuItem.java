package fishgame.minecraftFish.menu;

import fishgame.minecraftFish.util.InventorySerializer;
import org.bukkit.inventory.ItemStack;

public class MenuItem {

    private final int id;
    private final String name;
    private final int size;
    private final String menuInventory;
    private final ItemStack[] menuInventoryAsItemStack;

    public MenuItem(int id, String name, int size, String menuInventory) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.menuInventory = menuInventory;
        this.menuInventoryAsItemStack = InventorySerializer.inventoryFromJSON(menuInventory);
    }

    public int getId() { return id;}
    public String getName() { return name;}
    public int getSize() { return size;}
    public String getMenuInventoryAsString() { return menuInventory;}
    public ItemStack[] getMenuInventoryAsItemStack() { return menuInventoryAsItemStack;}
}
