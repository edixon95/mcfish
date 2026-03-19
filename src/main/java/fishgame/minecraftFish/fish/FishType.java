package fishgame.minecraftFish.fish;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FishType {

    private final String name;
    private final Material material;
    private final int rarityWeight;
    private final int baseGoldValue;
    private final int fishPower;

    public FishType(String name, Material material, int rarityWeight, int baseGoldValue, int fishPower) {
        this.name = name;
        this.material = material;
        this.rarityWeight = rarityWeight;
//        this.customModelData = customModelData;
        this.baseGoldValue = baseGoldValue;
        this.fishPower = fishPower;
    }

    public String getName() {
        return name;
    }

    public int getBaseGoldValue() {
        return baseGoldValue;
    }

    public int getRarityWeight() {
        return rarityWeight;
    }

    public int getFishPower() { return fishPower;}

    public ItemStack getItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
//            meta.setCustomModelData(customModelData);
            item.setItemMeta(meta);
        }
        return item;
    }
}