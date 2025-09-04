package securechat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import securechat.database.DatabaseHandler;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private DatabaseHandler db;

    public LoginGUI() {
        db = new DatabaseHandler();
        db.connect();

        setTitle("Secure Chat Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Components
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        add(loginButton);
        add(registerButton);

        // Button actions
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (db.validateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            openChat(username);
        } else {
            JOptionPane.showMessageDialog(this, "Login failed! Check credentials.");
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (db.addUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! Username may exist.");
        }
    }

    private void openChat(String username) {
        // Open main chat GUI
        new ChatClient("localhost", 5050, username);
        dispose(); // close login window
    }

    public void closeDB() {
        db.close();
    }

    // For testing
    public static void main(String[] args) {
        new LoginGUI();
    }
}
