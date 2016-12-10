package server.implementation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private static final int MAX_PREFIX_SIZE = 15;
    private static final String SERVER_NAME = "Server";

    private Date timestamp = null;
    private String message;
    private String emitter;

    public Message(String message, String emitter) {
        this.message = message;
        this.emitter = emitter == null ? SERVER_NAME : emitter;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        return String.format("<html><span>%s&nbsp;&nbsp;<b>%" + MAX_PREFIX_SIZE + "s</b>:&nbsp;%s</span></html>",
                dateFormat.format(timestamp),
                emitter,
                message);
    }
}
