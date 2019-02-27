package org.ir.agents.compteur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * <p>
 * Un agent compteur. Il contient les valeurs du compteur de texte et d'image.
 * </p>
 * <p>
 * On utilise deux compteurs pour éviter d'impacter le poid d'un chemin en cas
 * d'erreur.
 * </p>
 * <p>
 * Par exemple si une requête pour le texte atteint le serveur d'image, on ne
 * veut pas augmenter le compteur pour le texte. Cependant on ne veut pas
 * impacter les pheromones des images. Par conséquence le chemin menant au
 * mauvais serveur pour le texte perdera de son poid petit à petit sans le
 * réduire pour les images.
 * </p>
 * 
 * @version 1.0
 */
public class App {

    /**
     * Un compteur pour les pheromones du texte.
     */
    private static Compteur compteurText = new Compteur();
    /**
     * Un compteur pour les pheromones des images.
     */
    private static Compteur compteurImage = new Compteur();
    /**
     * Runnable exécutant la logique du serveur.
     */
    private static Runnable serveur = new Runnable() {

        private void handling(ServerSocket socket) throws IOException {
            while (true) {
                Socket socketClient = socket.accept();
                PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                String str = in.readLine();
                switch (str) {
                case "T+":
                    compteurText.incrementer();
                    out.println(compteurText.getPH());
                    System.out.println("Inc Text ok...");
                    break;
                case "T-":
                    compteurText.decrementation();
                    out.println(compteurText.getPH());
                    System.out.println("Dec Text ok...");
                    break;
                case "I+":
                    compteurImage.incrementer();
                    out.println(compteurImage.getPH());
                    System.out.println("Inc Image ok...");
                    break;
                case "I-":
                    compteurImage.decrementation();
                    out.println(compteurImage.getPH());
                    System.out.println("Dec Image ok...");
                    break;
                case "T":
                    out.println(compteurText.getPH());
                    System.out.println("print Text " + compteurText.getPH());
                    break;
                case "I":
                    out.println(compteurImage.getPH());
                    System.out.println("print Image " + compteurImage.getPH());
                    break;
                default:
                    System.err.println("Erreur entrée invalide");
                    out.println(-1);
                }
            }
        }

        @Override
        public void run() {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(1103);
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
    /**
     * Thread sur lequel tourne le serveur.
     */
    private static Thread serveurThread;

    public static void start() throws InterruptedException {
        serveurThread = new Thread(serveur);
        serveurThread.start();
        serveurThread.join();
    }

    public static void kill() {
        serveurThread.interrupt();
    }

    public static void main(String[] args) {
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
