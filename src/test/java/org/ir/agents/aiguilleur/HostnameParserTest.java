package org.ir.agents.aiguilleur;

import org.junit.Assert;
import org.junit.Test;

public class HostnameParserTest {

    @Test
    public void testGetNextHostnames() {
        String hostname = "frodon20";

        Pair<String> hostnames = HostnameParser.getNextHostnames(hostname);

        Assert.assertEquals("frodon10", hostnames.first());
        Assert.assertEquals("frodon11", hostnames.second());

        hostname = "frodon10";
        hostnames = HostnameParser.getNextHostnames(hostname);

        Assert.assertEquals("frodon05", hostnames.first());
        Assert.assertEquals("frodon06", hostnames.second());
    }
}
