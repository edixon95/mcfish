package fishgame.minecraftFish.database;

import fishgame.minecraftFish.player.FishPlayer;

import java.sql.*;
import java.util.UUID;

public class PlayerRepository {

    private final Database database;

    public PlayerRepository(Database database) {
        this.database = database;
    }

    public void createPlayer(UUID uuid, String name) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement(
                "INSERT INTO players (uuid, name, fish_caught, currency1, currency2) VALUES (?, ?, 0, 0, 0)"
        );

        stmt.setString(1, uuid.toString());
        stmt.setString(2, name);
        stmt.executeUpdate();
    }

    public ResultSet getPlayerFromDatabase(UUID uuid) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement(
                "SELECT * FROM players WHERE uuid = ?"
        );
        stmt.setString(1, uuid.toString());
        return stmt.executeQuery();
    }

    public void savePlayerToDatabase(FishPlayer player, String inventoryJSON) throws SQLException {
        PreparedStatement stmt = database.getConnection().prepareStatement(
                "UPDATE players SET fish_caught = ?, inventory = ?, currency1 = ?, currency2 = ? WHERE uuid = ?"
        );

        stmt.setInt(1, player.getFishCaught());
        stmt.setString(2, inventoryJSON);
        stmt.setInt(3, player.getGold());
        stmt.setInt(4, player.getPremium());
        stmt.setString(5, player.getUuid().toString());

        stmt.executeUpdate();
        stmt.close();
    }
}