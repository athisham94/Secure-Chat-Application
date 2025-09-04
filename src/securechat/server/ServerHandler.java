package securechat.server;

import java.io.*;
import java.net.*;
import java.util.List;
import securechat.database.DatabaseHandler;

public class ServerHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private DatabaseHandler db;
    private List<PrintWriter> clientWriters;

    private String username;

    public ServerHandler(Socket socket, DatabaseHandler db, List<PrintWriter> clientWriters) {
        this.clientSocket = socket;
        this.db = db;
        this.clientWriters = clientWriters;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Add this client's writer to the broadcast list
            synchronized (clientWriters) {
                clientWriters.add(out);
            }

            // First message from client is the username
            username = in.readLine();
            System.out.println("👤 User logged in: " + username);

            String msg;
            while ((msg = in.readLine()) != null) {
                // Message format: receiver:message
                String[] parts = msg.split(":", 2);
                if (parts.length < 2) continue;

                String receiver = parts[0];
                String message = parts[1];

                // Save to database
                db.saveMessage(username, receiver, message);

                // Broadcast to all clients
                synchronized (clientWriters) {
                    for (PrintWriter writer : clientWriters) {
                        if (writer == out) {
                            writer.println("Me -> " + receiver + ": " + message);
                        } else {
                            writer.println(username + ": " + message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("🔒 Connection closed for " + username);
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
            synchronized (clientWriters) { clientWriters.remove(out); }
        }
    }
}
