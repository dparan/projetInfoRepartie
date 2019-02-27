package org.ir.agents.lanceur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ReqRunnable
 */
public class ReqRunnable implements Runnable {
    private Socket socket;
    private String path;
    private PrintWriter out;
    private BufferedReader in;

    public void configure(String path) {
        // valeur qui sera envoyé aux aiguilleurs
        // "text" ou "image"
        this.path = path;
    }

    public void parseAndDisplayMessage(String message) {
        String data[] = message.split("-");
        int direction = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        String messageContent = data[2];
        System.out.println(direction + "\n" + id + "\n" + messageContent);
    }

    @Override
    public void run() {
        // Prise en charge de la demande d'image
        try {
            socket = new Socket("localhost", 1102);
            out = new PrintWriter(socket.getOutputStream(), true);
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