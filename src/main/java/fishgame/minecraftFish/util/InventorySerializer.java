package fishgame.minecraftFish.util;

import org.bukkit.inventory.*;

import org.bukkit.inventory.ItemStack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class InventorySerializer {

    private static final Gson gson = new Gson();

    /**
     * Convert PlayerInventory to JSON string
     */
    public static String inventoryToJSON(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        List<Map<String, Object>> serialized = new ArrayList<>();

        for (ItemStack item : contents) {
            if (item != null) {
                Map<String, Object> itemMap = item.serialize();
                serialized.add(itemMap);
            } else {
                serialized.add(null);
            }
        }

        return gson.toJson(serialized);
    }

    public static ItemStack[] inventoryFromJSON(String json) {
        if (json == null || json.isEmpty()) return new ItemStack[0];

        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> serialized = gson.fromJson(json, listType);

        ItemStack[] contents = new ItemStack[serialized.size()];

        for (int i = 0; i < serialized.size(); i++) {
            Map<String, Object> itemMap = serialized.get(i);
            if (itemMap != null) {
                contents[i] = ItemStack.deserialize(itemMap);
            } else {
                contents[i] = null;
            }
        }

        return contents;
    }

}