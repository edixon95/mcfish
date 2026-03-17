package fishgame.minecraftFish.database;

import fishgame.minecraftFish.fish.FishType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

public class Database {

    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/fish_game";
    private final String username = "root";
    private final String password = "test";

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) return;
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Database connected!");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}