package org.ir.agents.compteur;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Unit test for App.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    private static Socket client;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Thread t;

    /**
     * Préparation du serveur pour les tests
     * 
     * @throws UnknownHostException
     * 
     */
    @BeforeClass
    public static void setupBefore() throws UnknownHostException {
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    App.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    /**
     * Libération de la mémoire après les tests
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    @AfterClass
    public static void cleanAfter() throws IOException, InterruptedException {
        if (client != null) {
            client.close();
            in.close();
            out.close();
        }
        t.interrupt();
        t.join();
        App.kill();
    }

    /**
     * Test l'incrémentation d'un texte.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void AincrementTextTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 15.2;
        out.println("T+");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test la décrémentation d'un texte.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void BdecrementTextTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 13.68;
        out.println("T-");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test la récupération d'un texte.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void CgetTextTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 13.68;
        out.println("T");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test l'incrémentation d'une image.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void DincrementImageTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 15.2;
        out.println("I+");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test la décrémentation d'une image.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void EdecrementImageTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 13.68;
        out.println("I-");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test la récupération d'une image.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test
    public void FgetImageTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        double expected = 13.68;
        out.println("I");
        String res = in.readLine();
        System.out.println(res);
        double actual = Double.parseDouble(res);
        assertEquals(expected, actual, .0);
    }

    /**
     * Test erreur.
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @Test(expected=IOException.class)
    public void HerrorTest() throws NumberFormatException, IOException
    {   
        client = new Socket("localhost", 1103);
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out.println("c,hj");
        String res = in.readLine();
        if (Integer.parseInt(res) == -1){
            throw new IOException();
        }
    }
}
