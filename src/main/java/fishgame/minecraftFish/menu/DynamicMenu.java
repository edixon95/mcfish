package fishgame.minecraftFish.menu;

import fishgame.minecraftFish.player.FishPlayer;
import fishgame.minecraftFish.player.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class DynamicMenu {

    private Inventory createDynamicMenu(MenuItem menu, FishPlayer fp) {
        Inventory inventory = Bukkit.createInventory(
                null,
                menu.getSize(),
                menu.getName()
        );

        ItemStack[] items = menu.getMenuInventoryAsItemStack();
        ItemStack[] enrichedMenuItems = applyVariables(items, fp);
        inventory.setContents(enrichedMenuItems);

        return inventory;
    }

    private ItemStack[] applyVariables(ItemStack[] items, FishPlayer fp) {
        ItemStack[] result = new ItemStack[items.length];

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null) continue;

            ItemStack clone = item.clone();
            ItemMeta meta = clone.getItemMeta();
            if (meta == null) {
                result[i] = clone;
                continue;
            }

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey upgradeKey = new NamespacedKey("minecraftfish", "upgrade_id");
            String upgradeIntegerAsString = data.get(upgradeKey, PersistentDataType.STRING);

            if (upgradeIntegerAsString == null) {
                result[i] = clone;
                continue;
            }
            int upgradeId = Integer.parseInt(upgradeIntegerAsString);
            if (meta.hasLore()) {
                List<String> newLore = new ArrayList<>();

                for (String line : meta.getLore()) {
                    newLore.add(parseUpgradeLine(line, fp, upgradeId));
                }

                meta.setLore(newLore);
            }

            if (meta.hasDisplayName()) {
                meta.setDisplayName(
                        parseUpgradeLine(meta.getDisplayName(), fp, upgradeId)
                );
            }

            clone.setItemMeta(meta);
            result[i] = clone;
        }

        return result;
    }

    private String parseUpgradeLine(String line, FishPlayer fp, int upgradeId) {
        Upgrade upgrade = fp.getUpgradeById(upgradeId);

        if (upgrade != null) {
            line = line.replace("{name}", upgrade.getName())
                    .replace("{level}", String.valueOf(upgrade.getLevel()))
                    .replace("{cost}", String.valueOf(upgrade.getScaledPrice()));

        }

        return line;
    }

    public void handleGetMenu(MenuItem menu, Player player, FishPlayer fp) {
        Inventory menuInv = createDynamicMenu(menu, fp);
        player.openInventory(menuInv);
    }
}
