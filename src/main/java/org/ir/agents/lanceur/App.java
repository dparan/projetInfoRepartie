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
    private static ReqRunnable imageRunnable = new ReqRunnable();
    private static ReqRunnable texteRunnable = new ReqRunnable();
    private static Runnable serveur = new Runnable() {

        @Override
        public void run() {
            try {
                ServerSocket socket = new ServerSocket(1102);
                while (true) {
                    Socket socketClient = socket.accept();
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

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

    private static void display() {
        System.out.println("1-\tImage\n2-\tTexte\n3-\tQuitter\n");
    }

    public static void main(String[] args) {

        Thread serveurThread = new Thread(serveur);
        serveurThread.start();

        imageRunnable.configure("image");
        texteRunnable.configure("texte");

        Scanner sc = new Scanner(System.in);
        while (true) {
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
