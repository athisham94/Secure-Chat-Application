package securechat.client;

import java.awt.*;
import javax.swing.*;

public class ClientGUI extends JFrame {

    private ChatClient client;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JTextField receiverField;

    public ClientGUI(ChatClient client) {
        this.client = client;
        setTitle("Secure Chat App");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with input and send button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        receiverField = new JTextField("Receiver");
        bottomPanel.add(receiverField, BorderLayout.WEST);

        inputField = new JTextField();
        bottomPanel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listener for Send button
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private void sendMessage() {
        String receiver = receiverField.getText().trim();
        String message = inputField.getText().trim();
        if (!receiver.isEmpty() && !message.isEmpty()) {
            client.sendMessage(receiver, message);
            inputField.setText("");
            // DO NOT append locally — server will send it back
        }
    }

    // Method to display messages received from server
    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
