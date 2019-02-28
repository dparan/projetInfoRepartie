package org.ir.agents.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Objects;

/**
 * AgentMobile
 */
public class AgentMobile extends ObjectInputStream {
    private final String id;
    private final String type;
    private boolean direction = false;
    private String value = null;
    private ClassLoader remoteObjectClassLoader = null;
    private static final int MAX = Integer.MAX_VALUE;

    public AgentMobile(String type, InputStream is, URL classpath) throws UnknownHostException, IOException {
        super(is);
        URL[] urls = {classpath};
        remoteObjectClassLoader = new URLClassLoader(urls);

        InetAddress inetAddress = InetAddress.getLocalHost();
        int nombreAleatoire = (int) (Math.random() * MAX);
        Date now = new Date();
        long nowTimeStamp = now.toInstant().toEpochMilli();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inetAddress.getHostName()).append(nombreAleatoire).append(nowTimeStamp);

        this.id = stringBuilder.toString();
        this.type = type;
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        return remoteObjectClassLoader.loadClass(desc.getName());
    }

    public String getId() {
        return this.id;
    }


    public String getType() {
        return this.type;
    }

    public boolean getDirection() {
        return this.direction;
    }

    public String getValue() {
        return this.value;
    }

    public void changeDirection() {
        direction = !direction;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AgentMobile)) {
            return false;
        }
        AgentMobile agentMobile = (AgentMobile) o;
        return Objects.equals(id, agentMobile.id) && Objects.equals(type, agentMobile.type) && direction == agentMobile.direction && Objects.equals(value, agentMobile.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, direction, value);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", direction='" + getDirection() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }


}