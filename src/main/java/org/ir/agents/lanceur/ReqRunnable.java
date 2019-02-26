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
        this.path = path;
    }

    @Override
    public void run() {
        // prise en charge de la demande d'image
        try {
            socket = new Socket("localhost", 1102);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(path);
            String res = in.readLine();
            System.out.println(res);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}