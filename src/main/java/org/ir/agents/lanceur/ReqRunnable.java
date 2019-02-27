package org.ir.agents.lanceur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
     * Chaine de caractères sous la forme <direction>-<id>-<message>, ex: 0-1-text. Ce path est modifiable
     * </p>
     *
     * @see ReqRunnable#configure
     *
     */
    private String path;
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
     *
     * <p>
     * Lecture du texte d'un flux de saisie de caractères
     * </p>
     *
     */
    private BufferedReader in;

    /**
     * Met à jour le path envoyé
     * 
     * @param path Le nouveau path à envoyer
     **/

    public void configure(String path) {
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
        int direction = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        String messageContent = data[2];
        System.out.println(direction + "\n" + id + "\n" + messageContent);
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
            /**
             * <p>
             * Création d'un flux d'entrée de caractère en mémoire.
             * </p>
             * 
             * @parm InputStreamReader Permettre la conversion d'octes en caractères.
             * @parm getInputStream Retourne un flux d'entrée pour cette socket.
             * @see ReqRunnable#BufferdReader
             * @see ReqRunnable#socket
             */
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Ecriture du message dans le flux de sortie
            out.println(path);
            // Trace pour le debug
            String res = in.readLine();
            parseAndDisplayMessage(res);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}