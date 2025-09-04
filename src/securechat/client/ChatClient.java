package securechat.client;

import java.io.*;
import java.net.*;

public class ChatClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private ClientGUI gui;

    public ChatClient(String serverAddress, int port, String username) {
        this.username = username;
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send username to server
            out.println(username);

            // Start GUI
            gui = new ClientGUI(this);

            // Listen for incoming messages
            new Thread(new IncomingReader()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String receiver, String message) {
        out.println(receiver + ":" + message);
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    gui.displayMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
    }
}
