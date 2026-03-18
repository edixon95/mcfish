package fishgame.minecraftFish.database;

import fishgame.minecraftFish.fish.FishType;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FishRepository {

    private final Database database;

    public FishRepository(Database database) {
        this.database = database;
    }

    public List<FishType> getAllFish() throws SQLException {
        List<FishType> fishList = new ArrayList<>();
        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM fish");

        while (rs.next()) {
            String name = rs.getString("name");
            String materialString = rs.getString("material");

            Material material;
            try {
                material = Material.valueOf(materialString.toUpperCase());
            } catch (IllegalArgumentException e) {
                material = Material.COD;
                System.out.println("Unknown material: " + materialString);
            }

            int weight = rs.getInt("weight");
            int value = rs.getInt("base_gold_value");
            int fishPower = rs.getInt("fish_power");

            fishList.add(new FishType(name, material, weight, value, fishPower));
        }

        return fishList;
    }
}