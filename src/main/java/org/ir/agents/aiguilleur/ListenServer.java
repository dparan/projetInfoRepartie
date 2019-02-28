package org.ir.agents.aiguilleur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenServer implements Runnable {

    public static final int PORT = 1102;

    private static final Logger LOGGER = Logger.getLogger("ListenServer");

    private ServerSocket server;

    public ListenServer() throws IOException {
        server = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "ListenServer started, and listening on port {0}.", PORT);

        try {
            while (true) {
                Socket sender = server.accept();
                Thread worker = new Thread(new Router(sender));
                worker.start();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Thread serverThread = new Thread(new ListenServer());
            serverThread.start();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
