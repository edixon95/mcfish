package fishgame.minecraftFish.game;

import fishgame.minecraftFish.database.MenuRepository;
import fishgame.minecraftFish.menu.MenuItem;

import java.sql.SQLException;
import java.util.*;

public class MenuManager {

    private final MenuRepository menuRepository;
    private final List<MenuItem> menuItems = new ArrayList<>();

    public MenuManager(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        loadMenusFromDatabase();
    }

    private void loadMenusFromDatabase() {
        try {
            menuItems.addAll(menuRepository.getAllMenus());

            if (menuItems.isEmpty()) {
                menuItems.add(new MenuItem(1, "Filler", 9,"[]"));
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

}
