package org.ir.agents.lanceur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.Scanner;

/**
 * Agent lanceur
 * 
 *
 * Permet de choisir le type de requête à propager Les choix sont : 1 - Images 2
 * - Texte Les réponses attendues sont : 1 - Une Image 2 - Du Texte
 *
 * @version 1.0
 */
public class App {
    /**
     *
     * <p>
     * Variable statique représentant une instance de la classe ReqRunnable pour les
     * images
     * </p>
     * 
     * @see ReqRunnable
     *
     */
    private static ReqRunnable imageRunnable = new ReqRunnable();
    /**
     *
     * <p>
     * Variable statique représentant une instance de la classe ReqRunnable pour le
     * texte
     * </p>
     * 
     * @see ReqRunnable
     *
     */
    private static ReqRunnable texteRunnable = new ReqRunnable();

    /**
     *
     * <p>
     * Methode permetant l'affichage
     * </P>
     *
     *
     */
    private static void display() {
        System.out.println("1-\tImage\n2-\tTexte\n3-\tQuitter\n");
    }

    /**
     * Méthode permettant d'afficher un message reçu des aiguilleurs
     * 
     * @param message reçu depuis un socket d'aiguilleur
     */
    public static void parseAndDisplayMessage(String message) {
        String data[] = message.split("-");
        Message parsedMessage;
        try {
            parsedMessage = new Message(data[1], data[2], Integer.parseInt(data[0]));
            parsedMessage.setValue(data[3]);
            if (parsedMessage.getValue().equals("notFound")) {
                throw new FileNotFoundException();
            }
            File filesave = new File(parsedMessage.getId() + (data[2].equals("text") ? ".txt" : ".jpg"));
            FileOutputStream fileOutputStream = new FileOutputStream(filesave);
            fileOutputStream.write(Base64.getDecoder().decode(parsedMessage.getValue()));
            System.out.println("Fichier sauvegardé à " + filesave.getAbsolutePath());
            fileOutputStream.close();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Runnable serveurRunnable = new Runnable(){
    
        @Override
        public void run() {
            ServerSocket serveur = null;
            try {
                serveur = new ServerSocket(1101);
                while(true) {
                    Socket socket = serveur.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    parseAndDisplayMessage(in.readLine());
                }
            } catch (IOException e) {
                if (serveur != null) {
                    try {
                        serveur.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
    };

    private static Thread serveurThread = null;

    public static void main(String[] args) {
        serveurThread = new Thread(serveurRunnable);
        serveurThread.start();
        Message imageMessage = null;
        Message textMessage = null;
        /**
         *
         * <p>
         * Traitement de la saisie
         * </p>
         *
         *
         */
        Scanner sc = new Scanner(System.in);
        while (true) {
            // affichage du menu et saisie de la valeur
            display();
            while (!sc.hasNextInt()) {
                /**
                 * <p>
                 * Prise en charge des erreurs de saisie.
                 * </p>
                 * <p>
                 * l'entrée n'est pas un entier.
                 * </p>
                 */
                System.out.println("Veuillez saisir 1, 2 ou 3");
                display();
                sc.nextLine();
            }
            int num = sc.nextInt();
            Thread reqThread = null;
            try {
                imageMessage = new Message("image");
                textMessage = new Message("text");
            } catch (UnknownHostException e1) {
                e1.printStackTrace();         
            }
            /**
             *
             * @see ReqRunnable#path
             * @see ReqRunnable#configure
             *
             */

            imageRunnable.configure(imageMessage);
            /**
             *
             * @see ReqRunnable#path
             * @see ReqRunnable#configure
             *
             */
            texteRunnable.configure(textMessage);

            switch (num) {
            case 1:
                // On souhaite avoir une image
                reqThread = new Thread(imageRunnable);
                break;
            case 2:
                // On souhaite avoir du texte
                reqThread = new Thread(texteRunnable);
                break;
            case 3:
                // fin du programme
                serveurThread.interrupt();
                System.exit(0);
                break;
            default:
                // Erreur de saisie
                System.out.println("Veuillez saisir 1, 2 ou 3");
                sc.nextLine();
                break;
            }
            // lancement du thread
            if (reqThread != null) {
                reqThread.start();
                try {
                    reqThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    sc.close();
                }
            }
        }
    }
}
