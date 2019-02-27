package org.ir.agents.lanceur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Agent lanceur
 * 
 * @version 1.0
 *
 * Permet de choisir le type de requête à propager Les choix sont : 
 *          1 - Images 
 *          2 - Texte 
 * Les réponses attendues sont : 
 *          1 - Une Image
 *          2 - Du Texte
 *
 */
public class App {
    private static ReqRunnable imageRunnable = new ReqRunnable();
    private static ReqRunnable texteRunnable = new ReqRunnable();
    private static Runnable serveur = new Runnable() {

        @Override
        public void run() {
            
            try(ServerSocket socket = new ServerSocket(1102);){
                // Boucle infinie pour la récéption du message saisi par l'utilisateur
                while (true) {
                    Socket socketClient = socket.accept();
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                    // traitement de la saisie
                    switch (in.readLine()) {
                    case "text":
                        // l'utilisateur souhaite récupérer du texte
                        out.println("Du Texte");
                        break;
                    case "image":
                        // l'utilisateur souhaite récupérer une image
                        out.println("Une Image");
                        break;
                    default:
                        // Saisie incorrecte
                        out.println("error");
                    }
                    socketClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     *  Affichage du menu 
     */
    private static void display() {
        System.out.println("1-\tImage\n2-\tTexte\n3-\tQuitter\n");
    }

    public static void main(String[] args) {

        Thread serveurThread = new Thread(serveur);
        serveurThread.start();
        // configuration du type de message qui sera envoyé aux aiguilleurs
        imageRunnable.configure("0-0-image");
        texteRunnable.configure("0-0-text");
    
        Scanner sc = new Scanner(System.in);
        while (true) {
            // affichage du menu et saisie de la valeur
            display();
            while (!sc.hasNextInt()) {
                // prise en charge des erreurs de saisie
                // l'entrée n'est pas un entier
                System.out.println("Veuillez saisir 1, 2 ou 3");
                display();
                sc.nextLine();
            }
            int num = sc.nextInt();
            Thread reqThread = null;
            
            switch (num) {
            case 1:
                // On souhaite avoir une image 
                reqThread = new Thread(imageRunnable);
                break;
            case 2:
                // On souhaite avoir une image
                reqThread = new Thread(texteRunnable);
                break;
            case 3:
                // fin du programme 
                System.exit(0);
                break;
            default:
                //  Erreur de saisie
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
                }
            }
        }
    }
}
