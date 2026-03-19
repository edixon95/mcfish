package fishgame.minecraftFish.database;

import fishgame.minecraftFish.Misc.Rarity;
import fishgame.minecraftFish.fish.FishType;
import org.bukkit.Material;

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
}