package org.ir.agents.receveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AgentReceveur {

    private static final String PATH = "D:/ProjetM1";
    private static Runnable serveur = new Runnable() {

        /**
         * Méthode à exécuter par le thread
         * 
         * @param socket le socket de communication
         */
        private void handling(ServerSocket socket) throws IOException {
            while (true) {
                Receveur r = new Receveur();
                Socket socketClient = socket.accept();
                PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                //  récupération du message 
                String str = in.readLine();
                String str_arr[] = str.split("-");
                switch (str_arr[2]) {
                case "text":
                    // récupération et envoit du contenu d'un fichier texte
                    r.remplirTableau(PATH);
                    out.println(new Message(str_arr[1], r.getTextFile(r.getRandomElement()), 1));
                    break;
                case "image":
                    // récupération et envoit du contenu d'un fichier imagex
                    r.remplirTableau(PATH);
                    out.println(new Message(str_arr[1], r.getImageFile(r.getRandomElement()), 1));
                    break;
                default:
                    System.err.println("erreur");
                    out.println("erreur");
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
