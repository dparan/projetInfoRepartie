package org.ir.agents.compteur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

    final static Compteur compteurText = new Compteur();
    final static Compteur compteurImage = new Compteur();
    private static Runnable serveur = new Runnable() {
//
        @Override
        public void run() {
            try {
                ServerSocket socket = new ServerSocket(1102);
                while (true) {
                    Socket socketClient = socket.accept();
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                    String str = in.readLine();
                    switch (str) {
                        case "T+":
                            compteurText.incrementer();
                            System.out.println("Inc Text ok...");
                            break;
                        case "T-":
                            compteurText.decrementation();
                            System.out.println("Dec Text ok...");
                            break;
                        case "I+":
                            compteurImage.incrementer();
                            System.out.println("Inc Image ok...");
                            break;
                        case "I-":
                            compteurImage.decrementation();
                            System.out.println("Dec Image ok...");
                            break;
                        case "T":
                            out.println(compteurText.getPH());  
                            break;
                        case "I":
                            out.println(compteurImage.getPH());
                            break;
                        default:
                            System.out.println("Erreur");
                            break;
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    ;
    public static void main(String[] args) throws InterruptedException {

        Thread serveurThread = new Thread(serveur);
        serveurThread.start();
//        double ph = 0.5;
        serveurThread.join();
    }
}

