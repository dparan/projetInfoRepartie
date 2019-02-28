package org.ir.agents.receveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentReceveur {

    private static final String PATH = "/media/Donnees/ProjetM1";
    private static final Logger LOGGER = Logger.getGlobal();
    private static Runnable serveur = new Runnable() {

        /**
         * Méthode à exécuter par le thread
         * 
         * @param socket le socket de communication
         */
        private void handling(ServerSocket socket) throws IOException {
            LOGGER.log(Level.INFO, "Creating receiver");
            Receveur r = new Receveur();
            while (true) {
                Socket socketClient = socket.accept();
                PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                //  récupération du message 
                String str = in.readLine();
                String str_arr[] = str.split("-");
                LOGGER.log(Level.INFO, "Message received: {0}", new Message(str_arr[1], str_arr[2], Integer.parseInt(str_arr[0])));
                switch (str_arr[2]) {
                case "text":
                    // récupération et envoit du contenu d'un fichier texte
                    r.remplirTableau(PATH);
                    Message textMessage = new Message(str_arr[1], str_arr[2], 1);
                    textMessage.setValue(r.getTextFile(r.getRandomElement()));
                    out.println();
                    break;
                case "image":
                    // récupération et envoit du contenu d'un fichier imagex
                    r.remplirTableau(PATH);
                    Message imageMessage = new Message(str_arr[1], str_arr[2], 1);
                    imageMessage.setValue(r.getImageFile(r.getRandomElement()));
                    out.println(imageMessage);
                    break;
                default:
                    System.err.println("erreur");
                    out.println(new Message(str_arr[1], "erreur", 1));
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
                e.printStackTrace();
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
