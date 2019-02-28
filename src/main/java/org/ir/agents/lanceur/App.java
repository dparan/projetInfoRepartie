package org.ir.agents.lanceur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

/**
 * Agent lanceur
 * Permet de choisir le type de requête à propager.
 *
 * @version 1.0
 */
public class App implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger("AgentLanceur");

    private JFrame frame;
    private JRadioButton imageCheckBox;
    private JRadioButton textCheckBox;

    private ReqRunnable imageRunnable;
    private ReqRunnable texteRunnable;

    private App() {
        imageRunnable = new ReqRunnable();
        texteRunnable = new ReqRunnable();
    }

    private void parseAndDisplayMessage(String message) {
        String[] data = message.split("-");
        Message parsedMessage;
        try {
            parsedMessage = new Message(data[1], data[2], Integer.parseInt(data[0]));
            parsedMessage.setValue(data[3]);
            if (parsedMessage.getValue().equals("notFound")) {
                throw new FileNotFoundException();
            }
            File filesave = new File(parsedMessage.getId() + (data[2].equals("text") ? ".txt" : ".jpg"));
            FileOutputStream fileOutputStream = new FileOutputStream(filesave);
            fileOutputStream.write(Base64.getDecoder().decode(parsedMessage.getValue()));
            LOGGER.info("Fichier sauvegardé à " + filesave.getAbsolutePath());
            fileOutputStream.close();

            display(filesave);
        } catch (NumberFormatException | IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void display(File file) {
        try {
            // Si on a une image
            if (file.getAbsolutePath().endsWith(".jpg")) {

                JDialog dialog = new JDialog(frame, "Image");
                JLabel image = new JLabel(new ImageIcon(file.toURI().toURL()));
                dialog.add(image);
                dialog.pack();
                dialog.setVisible(true);

            // Si on a un texte
            } else if (file.getAbsolutePath().endsWith(".txt")) {

                JFrame dialog = new JFrame("texte");
                JTextArea textArea = new JTextArea(30, 30);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                String text = readTextFromFile(file);
                if (text != null || text.isEmpty()) text = "text is empty/null..";
                textArea.setText(text);

                dialog.add(scrollPane);
                dialog.pack();
                dialog.setVisible(true);
                dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            } else
                JOptionPane.showMessageDialog(frame,
                        "Impossible de reconnaître l'extension du fichier reçu.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);

        } catch (MalformedURLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private String readTextFromFile(File file) {
        try {
            String filename = file.getAbsolutePath();
            Path path = Paths.get(filename);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return String.join("", lines);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }

    private void startServer() {
        new Thread(() -> {
            ServerSocket serveur = null;
            try {
                serveur = new ServerSocket(1101);
                while(true) {
                    Socket socket = serveur.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    parseAndDisplayMessage(in.readLine());
                }
            } catch (IOException e) {
                if (serveur != null) {
                    try {
                        serveur.close();
                    } catch (IOException e1) {
                        LOGGER.severe(e.getMessage());
                    }
                }
                LOGGER.severe(e.getMessage());
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Thread reqThread;

            Message imageMessage = new Message("image");
            Message textMessage = new Message("text");

            imageRunnable.configure(imageMessage);
            texteRunnable.configure(textMessage);

            if (imageCheckBox.isSelected())
                reqThread = new Thread(imageRunnable);

            else if (textCheckBox.isSelected())
                reqThread = new Thread(texteRunnable);

            else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner image ou texte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            reqThread.start();
            reqThread.join();

        } catch (InterruptedException | UnknownHostException e1) {
            LOGGER.severe(e1.getMessage());
        }
    }

    private void displayGUI() throws UnknownHostException {
        frame = new JFrame("Agent Lanceur");
        JPanel mainPanel = new JPanel(new BorderLayout());

        // CENTER
        JPanel centerPanel = new JPanel(new BorderLayout());

        { // Center of CENTER
            JPanel gridPanel = new JPanel(new GridLayout(1, 2));

            ButtonGroup radioGroup = new ButtonGroup();

            imageCheckBox = new JRadioButton("Image");
            radioGroup.add(imageCheckBox);

            textCheckBox = new JRadioButton("Texte");
            textCheckBox.setSelected(true);
            radioGroup.add(textCheckBox);

            gridPanel.add(imageCheckBox);
            gridPanel.add(textCheckBox);

            centerPanel.add(gridPanel, BorderLayout.CENTER);
        }

        { // South of CENTER
            JPanel buttonPanel = new JPanel();
            JButton sendButton = new JButton("Lancer !");

            sendButton.addActionListener(this);

            buttonPanel.add(sendButton);
            centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // SOUTH
        JPanel southPanel = new JPanel();
        JLabel hostnameLabel = new JLabel("Hostname : " + InetAddress.getLocalHost().getHostName());
        southPanel.add(hostnameLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws UnknownHostException {
        App app = new App();
        app.startServer();
        app.displayGUI();
    }
}
