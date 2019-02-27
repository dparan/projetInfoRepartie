package org.ir.agents.receveur;

import java.io.IOException;
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

    /**
     * Remplis un tableau avec les chemins des fichiers
     * 
     * @param chemin le chemin du dossier à parcourir
     */
    public void remplirTableau(String chemin) throws IOException {
        // remplissage du tableau avec les fichiers
        Files.newDirectoryStream(Paths.get(chemin), path -> path.toString().contains(".")).forEach(tableau::add);
        for (Path s : Files.newDirectoryStream(Paths.get(chemin), path -> !path.toString().contains("."))) {
            // parcourt récursif des dossiers
            remplirTableau(s.toString());
        }
    }

    /**
     * Retourne le contenu d'un fichier texte aléatoire
     * 
     * @param chemin le chemin du fichier à retourner
     */
    public String getTextFile(String chemin) throws IOException {
        if (chemin.endsWith(".txt")) {
            // Contenu du texte
            StringBuilder stringBuilder = new StringBuilder();
            Stream<String> stream = Files.lines(Paths.get(chemin));
            // ajout de toutes les lignes du fichier dans le stringBuilder
            stream.forEach(stringBuilder::append);
            stream.close();
            return stringBuilder.toString();
        } else {
            return "notFound";
        }

    }
    /**
     * Retourne le contenu d'un fichier de type image
     * 
     * @param chemin le chemin du fichier à retourner
     */
    public String getImageFile(String chemin) throws IOException {
        // récuperation de tous les bits du fichier 
        byte[] fileContent = Files.readAllBytes(Paths.get(chemin));
        // encode en base 64 l'image et retourne la chaîne
        return Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * Récupération d'un élément aléatoire dans le tableau
     * 
     */
    String getRandomElement() {
        Random aleatoire = new Random();
        int size = tableau.size();
        int i = aleatoire.nextInt(size);
        return tableau.get(i).toString();
    }
}
