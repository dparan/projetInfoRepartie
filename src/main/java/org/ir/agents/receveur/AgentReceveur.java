package org.ir.agents.receveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AgentReceveur {

    private static Receveur TestText = new Receveur();
    private static Receveur TestImage = new Receveur();

    private static final String PATH = "D:/ProjetM1";
    private static Runnable serveur = new Runnable() {

        private void handling(ServerSocket socket) throws IOException {
            while (true) {
                Receveur r = new Receveur();
                Socket socketClient = socket.accept();
                PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                String str = in.readLine();
                String str_arr[] = str.split("-");
                switch (str_arr[2]) {
                    case "text":
                        r.displayText(PATH);
                        out.println(r.typeText(r.getRandomElement()));
                        break;
                    case "image":
                        r.displayText(PATH);
                        out.println(r.typeImage(r.getRandomElement()));
                        break;
                    default:
                        System.err.println("erreur");

                }
            }

        }

        @Override
        public void run() {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(1104);
                handling(socket);

            } catch (IOException e) {
                System.err.println("Fermeture du socket sur erreur");
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {

        Thread serveurThread = new Thread(serveur);
        serveurThread.start();
        serveurThread.join();

    }
}
