package org.ir.agents.lanceur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <p>
 * ReqRunnable implemente Runnable.
 * </p>
 * <p>
 * elle gere la communication avec le serveur.
 * </p>
 */
public class ReqRunnable implements Runnable {
    /**
     *
     * <p>
     * Permet a se connecter à une machine distante afin de communiquer
     * </p>
     *
     *
     */

    private Socket socket;
    /**
     * <p>
     * Message à transmettre.
     * Ce path est modifiable
     * </p>
     *
     * @see ReqRunnable#configure
     * @see Message
     *
     */
    private Message path;
    /**
     *
     * <p>
     * Impression des représentations formatées d'objets dans un flux de sortie de
     * texte.
     * </p>
     *
     *
     *
     */
    private PrintWriter out;

    private Runnable serveurRunnable = new Runnable(){
    
        @Override
        public void run() {
            ServerSocket serveur = null;
            try {
                serveur = new ServerSocket(1101);
                while (true) {
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

    private Thread serveurThread = null;
    

    /**
     * Met à jour le path envoyé
     * 
     * @param path Le nouveau path à envoyer
     **/

    public void configure(Message path) {
        // valeur qui sera envoyé aux aiguilleurs
        this.path = path;
    }

    /**
     * Méthode permettant d'afficher un message reçu des aiguilleurs
     * 
     * @param message reçu depuis un socket d'aiguilleur
     */
    public void parseAndDisplayMessage(String message) {
        String data[] = message.split("-");
        Message parsedMessage;
        try {
            parsedMessage = new Message(data[1], data[0], Integer.parseInt(data[2]));
            parsedMessage.setValue(data[3]);
            System.out.println(parsedMessage);
        } catch (NumberFormatException | UnknownHostException e) {
            e.printStackTrace();
        }
        serveurThread.interrupt();
    }

    @Override
    /**
     * Methode lancée par un thread
     * 
     *
     */
    public void run() {
        // Prise en charge de la demande d'image
        try {
            /**
             *
             * <p>
             * Création d'un socket de flux sur localhost et le connecte au numéro de port
             * 1102.
             * </p>
             * 
             * @see ReqRunnable#socket
             */
            socket = new Socket("localhost", 1102);
            /**
             * <p>
             * un nouveau PrintWriter d’un OutputStream existant.
             * </p>
             * 
             * @param getOutputStream Retourne un flux de sortie pour ce socket.
             * @see ReqRunnable#PrintWriter
             * @see ReqRunnable#socket
             */
            out = new PrintWriter(socket.getOutputStream(), true);
            // Ecriture du message dans le flux de sortie
            out.println(path);
            serveurThread = new Thread(serveurRunnable);
            serveurThread.start();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}