package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.MenuRepository;
import fishgame.minecraftFish.menu.DynamicMenu;
import fishgame.minecraftFish.menu.MenuItem;
import fishgame.minecraftFish.player.FishPlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;
import java.util.*;

public class MenuManager {

    private final MenuRepository menuRepository;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final DynamicMenu dynamicMenu = new DynamicMenu();
    private final GameManager gameManager;

    private static final NamespacedKey actionKey = new NamespacedKey("minecraftfish", "action");
    private static final NamespacedKey targetKey = new NamespacedKey("minecraftfish", "target");
    private static final NamespacedKey upgradeKey = new NamespacedKey("minecraftfish", "upgrade_id");

    public MenuManager(MenuRepository menuRepository, GameManager gameManager) {
        this.menuRepository = menuRepository;
        this.gameManager = gameManager;
        loadMenusFromDatabase();
    }

    public void processMenuClick(InventoryClickEvent event, Player player) {
        int slot = event.getSlot();

        if (event.getClickedInventory().equals(player.getInventory()) && slot <= 8) { return;}

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {return;}

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        int fullInvSlot = event.getRawSlot();
        String currentInvName = event.getView().getTitle();
        processMenuAction(meta, player, fullInvSlot, currentInvName);
    }

    private void loadMenusFromDatabase() {
        try {
            menuItems.addAll(menuRepository.getAllMenus());

            if (menuItems.isEmpty()) {
                String testMenuString = "[{\"DataVersion\":4671,\"id\":\"minecraft:birch_log\",\"count\":1,\"components\":{\"minecraft:custom_name\":\"{\\\"extra\\\":[{\\\"text\\\":\\\"Name: {name}\\\",\\\"color\\\":\\\"white\\\",\\\"bold\\\":0,\\\"italic\\\":0,\\\"underlined\\\":0,\\\"strikethrough\\\":0,\\\"obfuscated\\\":0}],\\\"text\\\":\\\"\\\"}\",\"minecraft:lore\":\"[{\\\"extra\\\":[{\\\"text\\\":\\\"Level: {level}\\\",\\\"color\\\":\\\"white\\\",\\\"bold\\\":0,\\\"italic\\\":0,\\\"underlined\\\":0,\\\"strikethrough\\\":0,\\\"obfuscated\\\":0}],\\\"text\\\":\\\"\\\"},{\\\"extra\\\":[{\\\"text\\\":\\\"Cost: {cost}\\\",\\\"color\\\":\\\"white\\\",\\\"bold\\\":0,\\\"italic\\\":0,\\\"underlined\\\":0,\\\"strikethrough\\\":0,\\\"obfuscated\\\":0}],\\\"text\\\":\\\"\\\"}]\",\"minecraft:custom_data\":\"{PublicBukkitValues:{\\\"minecraftfish:action\\\":\\\"purchase_upgrade\\\",\\\"minecraftfish:upgrade_id\\\":\\\"1\\\"}}\"},\"schema_version\":1},null,null,null,null,null,null,null,null]";
                menuItems.add(new MenuItem(1, "player_upgrades", 9, testMenuString));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processMenuAction(ItemMeta meta, Player player, int slot, String currentInv) {
        PersistentDataContainer data = meta.getPersistentDataContainer();
        String action = data.get(actionKey, PersistentDataType.STRING);
        if (action == null) return;
        FishPlayer fp = gameManager.getPlayerManager().handleGetPlayer(player.getUniqueId());
        switch (action) {
            case "navigate":
                String target = data.get(targetKey, PersistentDataType.STRING);
                if (target == null) return;
                player.sendMessage("Clicked to navigate to " + target);
                handleMenuNavigationClick(target, player, fp);
                break;

            case "purchase_upgrade":
                String upgradeIntegerAsString = data.get(upgradeKey, PersistentDataType.STRING);
                if (upgradeIntegerAsString == null) return; // Null check then treat as int for class simplicity
                int upgradeId = Integer.parseInt(upgradeIntegerAsString);
                handleMenuUpgradeClick(upgradeId, player, fp, currentInv, slot);
                player.sendMessage("Clicked to upgrade " + upgradeId);
                break;

            default:
                player.sendMessage("Action Type: \"" + action + "\" does not have a function");
                break;
        }
    }

    private MenuItem getMenuByTarget(String target) {
        return menuItems
                .stream()
                .filter(menu ->
                        menu.getName().equalsIgnoreCase(target)
                )
                .findFirst()
                .orElse(null);
    }

    private void handleMenuNavigationClick(String target, Player player, FishPlayer fp) {
        MenuItem menu = getMenuByTarget(target);
        if (menu == null) return;

        dynamicMenu.handleGetMenu(menu, player, fp);
    }

    private void handleMenuUpgradeClick(int upgradeId, Player player, FishPlayer fp, String menuString, int slot) {
        fp.addLevelsToUpgrade(upgradeId, 1);
        MenuItem menu = getMenuByTarget(menuString);
        dynamicMenu.rehydrateMenuSlot(player, fp, menu, slot);
    }




}
