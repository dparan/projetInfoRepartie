package org.ir.agents.aiguilleur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.ir.agents.aiguilleur.HostnameParser.NAME;

public class Router implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("Router");

    private static final int LAUNCHER_PORT  = 1101;
    private static final int COUNTER_PORT   = 1103;
    private static final int RECEIVER_PORT  = 1104;

    private static final Map<String, String> routes = new HashMap<>();

    private static String hostname;
    private static Pair<String> nextHostnames;

    private String senderHostname;
    private BufferedReader in;
    private PrintWriter out;

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            nextHostnames = HostnameParser.getNextHostnames(hostname);

            LOGGER.log(Level.INFO, "Hostname : {0}\nNext nodes hostnames : {1}, {2}.", new Object[]{hostname, nextHostnames.first(), nextHostnames.second()});
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    Router(Socket sender) throws IOException {
        senderHostname = sender.getInetAddress().getHostName();
        in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
        out = new PrintWriter(sender.getOutputStream(), true);

        LOGGER.log(Level.INFO, "New connection from {0}", senderHostname);
    }

    @Override
    public void run() {
        try {
            String message = in.readLine();
            String[] messages = message.split("-");

            LOGGER.log(Level.INFO, "Received message from {0}.", senderHostname);
            LOGGER.log(Level.INFO, "Message : {0}", message);

            // 0 = message descendant
            if (messages[0].equals("0")) {
                // On mémorise qui a envoyé le message
                routes.put(messages[1], senderHostname);
                LOGGER.log(Level.INFO, "Added route : ID={0} , Hostname={1}", new Object[]{messages[1], senderHostname});

                handleDescendingMessage(message);
            }

            // 1 = message montant
            else if (messages[0].equals("1"))
                handleAscendingMessage(message, messages[1]);

            // Message mal formé
            else
                LOGGER.log(Level.WARNING, "Malformed message from {0}.", senderHostname);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private void handleDescendingMessage(String message) throws IOException {
        LOGGER.info("Descending message.");

        // Si on est un poste receveur
        if (hostname.equals(NAME + "01") || hostname.equals(NAME + "02")) {
            LOGGER.info("Sending to message to local receiver...");
            handleLocalReceiver(message);

        } else {
            LOGGER.info("Asking next nodes for pheromons values .. ");

            // On récupère les valeurs de agents compteurs suivants
            Pair<Double> pheros = new Pair<>();

            for (String nextHostname : nextHostnames) {
                LOGGER.log(Level.INFO, "Connecting to {0}..", nextHostname);

                Socket nextCounter = new Socket(nextHostname, COUNTER_PORT);
                BufferedReader reader = new BufferedReader(new InputStreamReader(nextCounter.getInputStream()));
                PrintWriter writer = new PrintWriter(nextCounter.getOutputStream(), true);

                LOGGER.log(Level.INFO, "Sending decrementation and GET message.");
                
                if (message.contains("image")) {
                    writer.println("I-"); // Décrémentation
                    writer.println("I");  // Get

                } else if (message.contains("text")) {
                    writer.println("T-"); // Décrémentation
                    writer.println("T");  // Get
                }
                
                double phero = Double.parseDouble(reader.readLine());
                LOGGER.log(Level.INFO, "Received value {0} from hostname : {1}.", new Object[]{phero,nextHostname});
                pheros.add(phero);
                
                nextCounter.close();
            }


            String nextHostname = pickNextHostname(pheros);

            LOGGER.log(Level.INFO, "Next node picked : {0}", nextHostname);
            Socket socket = new Socket(nextHostname, ListenServer.PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            socket.close();
        }
    }

    private String pickNextHostname(Pair<Double> pheros) {
        double sum = pheros.first() + pheros.second();
        double firstBound = pheros.first() / sum;

        double pick = new Random().nextDouble();

        if (pick <= firstBound)
            return nextHostnames.first();
        else
            return nextHostnames.second();
    }

    private void handleLocalReceiver(String message) throws IOException {
        Socket agentRecepteur = new Socket(hostname, RECEIVER_PORT);

        PrintWriter writer = new PrintWriter(agentRecepteur.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(agentRecepteur.getInputStream()));

        writer.println(message);

        String received = reader.readLine();
        LOGGER.info("Received message from local receiver..");
        handleAscendingMessage(received, received.split("-")[1]);
        agentRecepteur.close();
    }

    private void handleAscendingMessage(String message, String id) throws IOException {
        LOGGER.info("Ascending message.");

        Socket socket = new Socket(hostname, COUNTER_PORT);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        
        // On incremente le compteur du noeud (courant)
        if (message.contains("image")) {
            writer.println("I+");
            LOGGER.log(Level.INFO, "Sending increment message to local counter agent.");
        }
        else if (message.contains("text")) {
            writer.println("T+");
            LOGGER.log(Level.INFO, "Sending increment message to local counter agent.");
        }

        // On recupere l'hostname du destinataire
        String receiverHostname = routes.get(id);
        LOGGER.log(Level.INFO, "Sending ascending message to {0}.", receiverHostname);

        // Si on ne trouve pas d'hostname dans les routes, cela veut dire qu'on est certainement le destinataire du message
        if ("localhost".equals(receiverHostname))
            // writer = new PrintWriter(new Socket(hostname, LAUNCHER_PORT).getOutputStream(), true);
            writer = out;
        else
            writer = new PrintWriter(new Socket(receiverHostname, ListenServer.PORT).getOutputStream(), true);

        writer.println(message);
        socket.close();
    }
}
