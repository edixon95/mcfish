package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.MenuRepository;
import fishgame.minecraftFish.menu.DynamicMenu;
import fishgame.minecraftFish.menu.MenuItem;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class MenuManager {

    private final MenuRepository menuRepository;
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final DynamicMenu dynamicMenu = new DynamicMenu();

    public MenuManager(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        loadMenusFromDatabase();
    }

    private void loadMenusFromDatabase() {
        try {
            menuItems.addAll(menuRepository.getAllMenus());

            if (menuItems.isEmpty()) {
                String testMenuString = "[{\"DataVersion\":4671,\"id\":\"minecraft:stone\",\"count\":1,\"components\":{\"minecraft:lore\":\"[{extra:[{bold:0b,color:\\\"gray\\\",italic:0b,obfuscated:0b,strikethrough:0b,text:\\\"Level: 5\\\",underlined:0b}],text:\\\"\\\"},{extra:[{bold:0b,color:\\\"dark_gray\\\",italic:0b,obfuscated:0b,strikethrough:0b,text:\\\"PDC test item\\\",underlined:0b}],text:\\\"\\\"}]\",\"minecraft:custom_name\":\"{extra:[{bold:0b,color:\\\"green\\\",italic:0b,obfuscated:0b,strikethrough:0b,text:\\\"Debug Stone\\\",underlined:0b}],text:\\\"\\\"}\",\"minecraft:custom_data\":\"{PublicBukkitValues:{\\\"minecraftfish:item_level\\\":5,\\\"minecraftfish:owner_uuid\\\":\\\"eb8e7d38-808c-4a89-8382-d0846984bba6\\\",\\\"minecraftfish:special_flag\\\":1b}}\"},\"schema_version\":1},null,null,null,null,null,null,null,null]";
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
        dynamicMenu.handleGetMenu(menu, player);
    }


}
