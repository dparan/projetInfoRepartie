package org.ir.agents.aiguilleur;

public class HostnameParser {

    public static final String NAME = "pixel";

    private HostnameParser() {}

    public static Pair<String> getNextHostnames(String hostname) {
        // Get host number from hostname
        int hostNumber = Integer.parseInt(hostname.replaceAll("[^0-9]+", ""));

        // Find next hostnames
        String firstNext = findNextHostname(hostNumber / 2);
        String secondNext = findNextHostname((hostNumber / 2) + 1);

        return new Pair<>(firstNext, secondNext);
    }

    private static String findNextHostname(int nextNumber) {
        if (nextNumber < 1) nextNumber = 1;
        
        String next = NAME;
        if (nextNumber < 10)
            next += "0";
        next += nextNumber;

        return next;
    }
}
