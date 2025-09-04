package securechat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import securechat.database.DatabaseHandler;

public class ChatServer {

    private static final int PORT = 5050;
    private static List<PrintWriter> clientWriters = Collections.synchronizedList(new ArrayList<>());
    private static DatabaseHandler db;

    public static void main(String[] args) {
        db = new DatabaseHandler();
        db.connect();

        System.out.println("✅ Connected to SQLite");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("🟢 Chat Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔵 New client connected: " + clientSocket.getInetAddress());

                ServerHandler handler = new ServerHandler(clientSocket, db, clientWriters);
                Thread t = new Thread(handler);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
