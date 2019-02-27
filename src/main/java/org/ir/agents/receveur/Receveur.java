package org.ir.agents.receveur;

import java.awt.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.stream.Stream;

public class Receveur {

    static ArrayList<Path> tableau = new ArrayList<>();
    static int i;

    public void displayText(String chemin) throws IOException {
        Files.newDirectoryStream(Paths.get(chemin), path -> path.toString().contains(".")).forEach(tableau::add);// mettre en tab & reccuperer un au hasard  
        for (Path s : Files.newDirectoryStream(Paths.get(chemin), path -> !path.toString().contains("."))) {
            displayText(s.toString());
        }
    }

    public String typeText(String text) throws IOException {
        if (text.endsWith(".txt")) {
            // Contenu du texte
            StringBuilder stringBuilder = new StringBuilder();
            Stream<String> stream = Files.lines(Paths.get(text));
            stream.forEach(stringBuilder::append);
            stream.close();
            return stringBuilder.toString();
        } else {
            return "notFound";
        }

    }

    public String typeImage(String text) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(text));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;

    }

    String getRandomElement() {
               Random aleatoire = new Random(); 
	    	 int size = tableau.size();
	    	 int i = aleatoire.nextInt(size);
	    return tableau.get(i).toString();  
    }
}
