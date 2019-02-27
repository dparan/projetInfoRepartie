package org.ir.agents.compteur;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * Unit test for Compteur
 */
public class CompteurTest 
{
    Compteur cpt;

    /**
     * Préparation du compteur pour les tests
     */
    @Before
    public void setupBefore() {
        cpt = new Compteur();
    }

    /**
     * Libération de la mémoire après les tests
     */
    @After
    public void cleanAfter() {
        cpt = null;
    }

    /**
     * Test l'incrémentation
     */
    @Test
    public void incrementTest()
    {
        cpt.incrementer();
        double expected = 15.2;
        double actual = cpt.getPH();
        assertEquals(expected, actual, .0);
    }

    /**
     * Test de la dérémentation
     */
    @Test
    public void decrementTest()
    {
        cpt.decrementation();
        double expected = 13.5;
        double actual = cpt.getPH();
        assertEquals(expected, actual, .0);
    }
}
