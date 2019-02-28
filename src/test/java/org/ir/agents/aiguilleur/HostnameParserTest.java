package org.ir.agents.aiguilleur;

import org.junit.Assert;
import org.junit.Test;

public class HostnameParserTest {

    @Test
    public void testGetNextHostnames() {
        String hostname = "gollum20";

        Pair<String> hostnames = HostnameParser.getNextHostnames(hostname);

        Assert.assertEquals("gollum10", hostnames.first());
        Assert.assertEquals("gollum11", hostnames.second());

        hostname = "gollum10";
        hostnames = HostnameParser.getNextHostnames(hostname);

        Assert.assertEquals("gollum05", hostnames.first());
        Assert.assertEquals("gollum06", hostnames.second());
    }
}
