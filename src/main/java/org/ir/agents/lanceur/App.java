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

    /**
     *  Affichage du menu 
     */
    private static void display() {
        System.out.println("1-\tImage\n2-\tTexte\n3-\tQuitter\n");
    }

    public static void main(String[] args) {
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
                    sc.close();
                }
            }
        }
    }
}
