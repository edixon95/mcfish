package fishgame.minecraftFish.database;

import fishgame.minecraftFish.Misc.Rarity;
import fishgame.minecraftFish.Misc.ServerConfig;
import fishgame.minecraftFish.player.Upgrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MiscRepository {

    private final Database database;

    public MiscRepository(Database database) {
        this.database = database;
    }

    public List<Rarity> getAllRarity() throws SQLException {
        List<Rarity> rarityList = new ArrayList<>();
        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM rarity_multiplier");

        while (rs.next()) {
            String name = rs.getString("name");
            double multiplier = rs.getDouble("multiplier");
            int weight = rs.getInt("weight");

            rarityList.add(new Rarity(name, multiplier, weight));
        }

        return rarityList;
    }

    public List<Upgrade> getAllUpgrades() throws SQLException {
        List<Upgrade> upgradeList = new ArrayList<>();
        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM upgrades");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double scale = rs.getDouble("scale");
            int baseCost = rs.getInt("base_cost");

            upgradeList.add(new Upgrade(id, name, baseCost, scale, 1));
        }

        return upgradeList;
    }

    public ServerConfig getServerConfig() throws SQLException {

        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM config");

        if (rs.next()) {
            int id = rs.getInt("id");
            String playerInventory = rs.getString("player_inventory");
            double version = rs.getDouble("version");

            return new ServerConfig(id, version, playerInventory);
        }

        return null;
    }
}