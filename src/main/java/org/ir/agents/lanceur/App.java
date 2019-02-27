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
 *          Permet de choisir le type de requête à propager Les choix sont : 1 -
 *          Images ou 2 - Texte Les réponses attendues sont : 1 - Une Image ou 2
 *          - Du Texte
 *
 */
public class App {
    /**
    *
    *       <p> Variable statique présente une instance de la classe ReqRunnable </p>
    *         @see  ReqRunnable
    *
    */
    private static ReqRunnable imageRunnable = new ReqRunnable();
     /**
    *
    *       <p> Variable statique présente une instance de la classe ReqRunnable </p>
    *       @see ReqRunnable
    *
    */
    private static ReqRunnable texteRunnable = new ReqRunnable();
     /**
    *
    *       <p> variable statique présente une instance de l'interface Runnable. </p>
    *         <p> implémentée par toute classe dont les instances sont destinées à être exécutées par un thread. </p>
    *
    */
    private static Runnable serveur = new Runnable() {

        @Override
        /**
            * <lu>Methode lancée par un thread </lu>
            *  <li>attend que les demandes arrivent sur le réseau. </li>
            * 
            *
        */
        public void run() {
            try {
                ServerSocket socket = new ServerSocket(1102);
                /**
                *   <p> Boucle while pour la reception des mesages saisi par l'utilisateur </p>
                *
                */
                while (true) {
                    /**
                    *
                    *  @see ReqRunnable#socket
                    */
                    Socket socketClient = socket.accept();
                    /**
                    *
                    *  @see ReqRunnable#PrintWriter
                    */
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                    /**
                    *
                    *   @see ReqRunnable#BufferedReader
                    */
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                    /**
                    *  <p> Traitement de la saisie </p>
                    *
                    */
                    switch (in.readLine()) {
                    case "texte":
                        out.println("Du Texte");
                        break;
                    case "image":
                        out.println("Une Image");
                        break;
                    default:
                        // error
                        out.println("error");
                    }
                    socketClient.close();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
     /**
    *
    *    <p> Methode parmet l'affichage </P> 
    *
    *
    */
    private static void display() {
        System.out.println("1-\tImage\n2-\tTexte\n3-\tQuitter\n");
    }

    public static void main(String[] args) {

         /**
    *
    *       <p> Instance d'un thread </p>
    *       @param serveur 
    *       @see App#serveur 
    *
    */
        Thread serveurThread = new Thread(serveur);
         /**
    *
    *       <p> Lancement du thread </p>
    *
    *
    */
        serveurThread.start();
         /**
    *
    *       @see ReqRunnable#path
    *       @see ReqRunnable#configure
    *
    */

        imageRunnable.configure("image");
         /**
    *
    *       @see ReqRunnable#path
    *       @see ReqRunnable#configure
    *
    */
        texteRunnable.configure("texte");
         /**
    *
    *       <p> Traitement de la saisie </p>
    *
    *
    */

        Scanner sc = new Scanner(System.in);
        while (true) {
            display();
            while (!sc.hasNextInt()) {
                /** <p>Prise en charge des erreurs de saisie. </p>
                * <p> l'entrée n'est pas un entier. </p>
                */
                System.out.println("Veuillez saisir 1, 2 ou 3");
                display();
                sc.nextLine();
            }
            int num = sc.nextInt();
            Thread reqThread = null;
            switch (num) {
            case 1:
                reqThread = new Thread(imageRunnable);
                break;
            case 2:
                reqThread = new Thread(texteRunnable);
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Veuillez saisir 1, 2 ou 3");
                sc.nextLine();
                break;
            }
            if (reqThread != null) {
                reqThread.start();
                try {
                    reqThread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
