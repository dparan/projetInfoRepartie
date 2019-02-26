package org.ir.agents.compteur;

public class Compteur {
    private double ph = 15;
    final static double ALPHA = 0.1;
    final static double BETA = 0.2;

    public synchronized void incrementer() {
        ph += BETA;
    }

    public synchronized void decrementation() {
        ph = (1 - ALPHA) * ph;
    }
    
    public double getPH() {
        return ph;
    }
}
