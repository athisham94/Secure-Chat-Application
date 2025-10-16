# Secure-Chat-Application
This project is a Java-based Secure Chat Application that enables safe real-time communication between users. It integrates AES/RSA encryption for message confidentiality and uses an SQLite database to manage user authentication and message storage.



## How to run 

1) In the same project root- **Run the Server**
   ```bash
   java -cp ".:out:lib/sqlite-jdbc-3.46.0.0.jar" securechat.server.ChatServer
2) Run the client (Login GUI)
   ```bash
   java -cp ".:out:lib/sqlite-jdbc-3.46.0.0.jar" securechat.Main
