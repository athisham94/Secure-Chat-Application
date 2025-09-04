package securechat;

import java.util.List;
import securechat.database.DatabaseHandler;

public class Main {
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        db.connect();

        // Test user registration
        db.addUser("alice", "12345");
        db.addUser("bob", "password");

        // Test login
        db.validateUser("alice", "12345");
        db.validateUser("bob", "wrongpass");

        // Test messages
        db.saveMessage("alice", "bob", "Hello Bob!");
        db.saveMessage("bob", "alice", "Hi Alice, how are you?");

        // Fetch chat history
        List<String> history = db.fetchMessages("alice", "bob");
        for (String msg : history) {
            System.out.println(msg);
        }

        db.close();
    }
}
