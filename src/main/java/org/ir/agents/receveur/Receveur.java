package org.ir.agents.receveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Receveur {

    static ArrayList<Path> tableau = new ArrayList<>();
    static int i;

    /**
     * Remplis un tableau avec les chemins des fichiers
     * 
     * @param chemin le chemin du dossier à parcourir
     */
    public void remplirTableau(String chemin) throws IOException {
        // on ne remplit le tableau qu'une fois
        if (tableau.isEmpty()) {
            // remplissage du tableau avec les fichiers
            Files.find(Paths.get(chemin), Integer.MAX_VALUE,(path,fileAttr) -> fileAttr.isRegularFile()).forEach((Path el) -> {
                if (!el.endsWith(".DS_Store")) {
                    tableau.add(el);
                }
            });
        }
    }

    private static StringBuilder getTxtFiles(InputStream in) {
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Retourne le contenu d'un fichier texte aléatoire
     * 
     * @param chemin le chemin du fichier à retourner
     */
    public String getTextFile(String chemin) throws IOException {
        if (chemin.endsWith(".zip")) {
            ZipFile zipFile = new ZipFile(chemin);
            for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                if (!entry.isDirectory()) {
                    StringBuilder stringBuilder = getTxtFiles(zipFile.getInputStream(entry));
                    zipFile.close();
                    // encode en base 64 l'image et retourne la chaîne
                    return Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes());
                }
            }
            zipFile.close();
        }
        return "notFound";
    }

    /**
     * Retourne le contenu d'un fichier de type image
     * 
     * @param chemin le chemin du fichier à retourner
     */
    public String getImageFile(String chemin) throws IOException {
    	if(chemin.endsWith(".jpg")) {
        	// récuperation de tous les bits du fichier 
        	byte[] fileContent = Files.readAllBytes(Paths.get(chemin));
        	// encode en base 64 l'image et retourne la chaîne
        	return Base64.getEncoder().encodeToString(fileContent);
		}
		return "notFound";
    }

    /**
     * Récupération d'un élément aléatoire dans le tableau
     * 
     */
    public String getRandomElement() {
        int i = (int) (Math.random() * tableau.size());
        return tableau.get(i).toString();
    }
}
