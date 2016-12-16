package server.implementation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Respresents a message
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class Message implements Serializable {
    /**
     * Maximum prefix size
     */
    private static final int MAX_PREFIX_SIZE = 15;

    /**
     * Name of the server
     */
    private static final String SERVER_NAME = "Server";

    /**
     * Timestamp of the message
     */
    private Date timestamp = null;

    /**
     * Content of the message
     */
    private String message;

    /**
     * Sender of the message
     */
    private String emitter;

    /**
     * Initialization constructor
     *
     * @param message see {@link #message}
     * @param emitter see {@link #emitter}
     */
    public Message(String message, String emitter) {
        this.message = message;
        this.emitter = emitter == null ? SERVER_NAME : emitter;
        this.timestamp = new Date();
    }

    /**
     * Converts the message to a string
     *
     * @return HTML Message
     */
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        return String.format("<html><span>%s&nbsp;&nbsp;<b>%" + MAX_PREFIX_SIZE + "s</b>:&nbsp;%s</span></html>",
                dateFormat.format(timestamp),
                emitter,
                message);
    }
}
