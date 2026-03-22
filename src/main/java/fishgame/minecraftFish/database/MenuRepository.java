package fishgame.minecraftFish.database;

import fishgame.minecraftFish.menu.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {

    private final Database database;

    public MenuRepository(Database database) {
        this.database = database;
    }

    public List<MenuItem> getAllMenus() throws SQLException {
        List<MenuItem> menuList = new ArrayList<>();
        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM menus");

        while (rs.next()) {

            int id = rs.getInt("id");
            String name = rs.getString("name");
            int size = rs.getInt("size");
            String menuInventory = rs.getString("menu_inventory");

            menuList.add(new MenuItem(id, name, size, menuInventory));
        }

        return menuList;
    }
}