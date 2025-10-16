package securechat.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private Connection conn;

    // Connect to SQLite
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:resources/chat.db");
            System.out.println("✅ Connected to SQLite");

            // Create tables if they don't exist
            String usersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT" +
                    ");";

            String messagesTable = "CREATE TABLE IF NOT EXISTS Messages (" +
                    "msg_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender TEXT," +
                    "receiver TEXT," +
                    "content TEXT," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            Statement stmt = conn.createStatement();
            stmt.execute(usersTable);
            stmt.execute(messagesTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add user (returns true if added, false if username exists)
    public boolean addUser(String username, String password) {
        try {
            String check = "SELECT username FROM Users WHERE username = ?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, username);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

            String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // You can later hash this
            ps.executeUpdate();

            System.out.println("👤 User added: " + username);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate login
    public boolean validateUser(String username, String password) {
        try {
            String sql = "SELECT username FROM Users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login success for " + username);
                return true;
            } else {
                System.out.println("❌ Login failed for " + username);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save message
    public void saveMessage(String sender, String receiver, String content) {
        try {
            String sql = "INSERT INTO Messages (sender, receiver, content) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, content);
            ps.executeUpdate();

            System.out.println("💬 Message saved: " + sender + " -> " + receiver);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch messages between two users
    public List<String> fetchMessages(String user1, String user2) {
        List<String> history = new ArrayList<>();
        try {
            String sql = "SELECT sender, receiver, content, timestamp FROM Messages " +
                    "WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                    "ORDER BY timestamp ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user1);
            ps.setString(2, user2);
            ps.setString(3, user2);
            ps.setString(4, user1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String msg = rs.getString("sender") + ": " + rs.getString("content") +
                        " [" + rs.getString("timestamp") + "]";
                history.add(msg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    // Close connection
    public void close() {
        try {
            if (conn != null) conn.close();
            System.out.println("🔒 Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
