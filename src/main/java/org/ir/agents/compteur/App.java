package org.ir.agents.compteur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentCompteur {

    final static Compteur compteur = new Compteur();
    private static Runnable serveur = new Runnable() {

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
                        case "inc":
                            compteur.incrementer();
                            System.out.println("Inc ok...");
                            break;
                        case "dec":
                            compteur.decrementation();
                            System.out.println("Dec ok...");
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

