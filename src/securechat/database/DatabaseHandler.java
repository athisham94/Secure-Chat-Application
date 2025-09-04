package securechat.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private Connection conn;

    
    public void connect() {
    try {
        Class.forName("org.sqlite.JDBC");  // ← load driver
        conn = DriverManager.getConnection("jdbc:sqlite:chat.db");
        System.out.println("✅ Connected to SQLite");
        createTables();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void createTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS messages (sender TEXT, receiver TEXT, message TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        stmt.close();
    }

    public boolean addUser(String username, String password) {
        try {
            String sql = "INSERT INTO Users(username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("👤 User added: " + username);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Could not add user: " + username);
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            boolean valid = rs.next();
            System.out.println(valid ? "✅ Login success for " + username : "❌ Login failed for " + username);
            return valid;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveMessage(String sender, String receiver, String message) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO messages(sender, receiver, message) VALUES (?, ?, ?)")) {
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, message);
            ps.executeUpdate();
            System.out.println("💬 Message saved: " + sender + " -> " + receiver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> fetchMessages(String user1, String user2) {
        List<String> history = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT sender, message, timestamp FROM messages WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?) ORDER BY timestamp")) {
            ps.setString(1, user1);
            ps.setString(2, user2);
            ps.setString(3, user2);
            ps.setString(4, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add(rs.getString("sender") + ": " + rs.getString("message") + " [" + rs.getString("timestamp") + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public void close() {
        try {
            if (conn != null) conn.close();
            System.out.println("🔒 Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
