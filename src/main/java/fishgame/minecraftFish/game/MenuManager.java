package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.MenuRepository;
import fishgame.minecraftFish.menu.DynamicMenu;
import fishgame.minecraftFish.menu.MenuItem;
import fishgame.minecraftFish.player.FishPlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class MenuManager {

    private final MenuRepository menuRepository;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final DynamicMenu dynamicMenu = new DynamicMenu();
    private final GameManager gameManager;

    public MenuManager(MenuRepository menuRepository, GameManager gameManager) {
        this.menuRepository = menuRepository;
        this.gameManager = gameManager;
        loadMenusFromDatabase();
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

    public MenuItem getMenuByTarget(String target) {
        return menuItems
                .stream()
                .filter(menu ->
                        menu.getName().equalsIgnoreCase(target)
                )
                .findFirst()
                .orElse(null);
    }

    public void handleMenuClick(String target, Player player) {
        MenuItem menu = getMenuByTarget(target);
        if (menu == null) return;
        FishPlayer fp = gameManager.getPlayerManager().handleGetPlayer(player.getUniqueId());
        dynamicMenu.handleGetMenu(menu, player, fp);
    }


}
