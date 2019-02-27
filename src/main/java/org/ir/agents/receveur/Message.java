package org.ir.agents.receveur;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Message
 */
public class Message {
    private final String id;
    private String content;
    private Integer direction = 0;
    private static final int MIN = 0;
    private static final int MAX = Integer.MAX_VALUE;

    public Message(String id, String content, Integer direction) throws UnknownHostException {
        if (id != null) {
            this.id = id;
        } else {
            InetAddress inetAddress = InetAddress.getLocalHost();
            int nombreAleatoire = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
            Date now = new Date();
            long nowTimeStamp = now.toInstant().toEpochMilli();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(inetAddress.getHostName()).append(nombreAleatoire).append(nowTimeStamp);
            this.id = stringBuilder.toString();
        }
        this.content = content;
        if (direction != null) {
            this.direction = direction;
        }
    }

    public Message(String content) throws UnknownHostException {
        this(null, content, null);
    }

    public Message(String content, int direction) throws UnknownHostException {
        this(null, content, direction);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(direction).append("-").append(id).append("-").append(content);
        return stringBuilder.toString();
    }

}