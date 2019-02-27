package org.ir.agents.compteur;

/**
 * Modèle d'un compteur.
 * Sa valeur par défaut 15
 * 
 * @version 1.0
 */
public class Compteur {
    /**
     * La valeur du compteur (pheromone)
     */
    private double ph = 15;
    /**
     * La valeur α utilisée pour le calcul de la décrémentation
     */
    public static final double ALPHA = 0.1;
    /**
     * La valeur β utilisée pour le calcul de l'incrémentation
     */
    public static final double BETA = 0.2;

    /**
     * Constructeur par défaut. Retourne une instance de Compteur ayant comme valeur
     */
    public Compteur() {}

    /**
     * Constructeur permettant de choisir la valeur initiale du compteur
     * 
     * @param ph double représentant la valeur initiale choisie
     */
    public Compteur(double ph) {
        this.ph = ph;
    }

    /**
     * Méthode d'incrémentation du compteur ph. Ph(t + 1) = Ph(t) + β
     * 
     */
    public synchronized void incrementer() {
        ph += BETA;
    }

    /**
     * Méthode de décrémentation du compteur ph. Ph(t + 1) = (1 - α) * Ph(t)
     */
    public synchronized void decrementation() {
        ph = (1 - ALPHA) * ph;
    }
    
    public double getPH() {
        return ph;
    }
}
