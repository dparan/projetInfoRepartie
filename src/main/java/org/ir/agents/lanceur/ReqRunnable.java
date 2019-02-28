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
    /**
     * Met à jour le path envoyé
     * 
     * @param path Le nouveau path à envoyer
     **/

    public void configure(Message path) {
        // valeur qui sera envoyé aux aiguilleurs
        this.path = path;
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
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}