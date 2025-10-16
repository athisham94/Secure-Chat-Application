package securechat;

import securechat.client.LoginGUI;
import securechat.database.DatabaseHandler;

public class Main {
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        db.connect();

        // Launch login GUI
        new LoginGUI(db);
    }
}
