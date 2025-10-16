package securechat.client;

import javax.swing.*;
import java.awt.*;
import securechat.database.DatabaseHandler;

public class LoginGUI extends JFrame {

    private DatabaseHandler db;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginGUI(DatabaseHandler db) {
        this.db = db;

        setTitle("Login - Secure Chat");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);
        registerButton = new JButton("Register");
        add(registerButton);

        // Action listeners
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (db.validateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "✅ Login success for " + username);
            openChatClient(username);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Login failed for " + username);
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        db.addUser(username, password);
        JOptionPane.showMessageDialog(this, "👤 User added: " + username);
    }

    private void openChatClient(String username) {
        // Connect to localhost server at port 5050
        new ClientGUI(new ChatClient("localhost", 5050, username));
        this.dispose(); // Close login window
    }
}
