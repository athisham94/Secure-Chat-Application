package securechat.client;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    
    public ChatClient(String host, int port, String username) {
        this.username = username;
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            
            out.println(username);

            
            new Thread(() -> listen()).start();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Unable to connect to server: " + host + ":" + port);
        }
    }

    
    public void sendMessage(String receiver, String message) {
        out.println(receiver + ":" + message);
    }

   
    private void listen() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                System.out.println(msg); // Or forward to GUI
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
