package org.ir.agents.lanceur;

import java.util.Scanner;

/**
 * Agent lanceur
 * 
 * @version 1.0
 *
 *          Permet de choisir le type de requête à propager Les choix sont : 1 -
 *          Images 2 - Texte Les réponses attendues sont : 1 - Une Image 2 - Du
 *          Texte
 *
 */
public class App {
    /**
     *
     * <p>
     * Variable statique représentant une instance de la classe ReqRunnable pour les images
     * </p>
     * 
     * @see ReqRunnable
     *
     */
    private static ReqRunnable imageRunnable = new ReqRunnable();
    /**
     *
     * <p>
     * Variable statique représentant une instance de la classe ReqRunnable pour le texte
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

    public static void main(String[] args) {
        /**
         *
         * @see ReqRunnable#path
         * @see ReqRunnable#configure
         *
         */

        imageRunnable.configure("0-0-image");
        /**
         *
         * @see ReqRunnable#path
         * @see ReqRunnable#configure
         *
         */
        texteRunnable.configure("0-0-text");
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
