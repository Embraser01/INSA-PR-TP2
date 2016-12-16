package server.implementation;

import java.io.*;
import java.util.ArrayList;

/**
 * Represents a room, with its persistent history, list of users and name
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class Room {

    /**
     * History file extension
     */
    private static final String HISTORY_EXT = ".history";

    /**
     * List of messages in the history
     */
    private ArrayList<Message> history = null;

    /**
     * List of users in the room
     */
    private ArrayList<Client> users = null;

    /**
     * Name of the room
     */
    private String name = null;

    /**
     * Initialization constructor, also loads the history from the file
     *
     * @param room Room name
     */
    @SuppressWarnings("unchecked")
    public Room(String room) {
        this.name = room;

        // Append history

        try {
            ObjectInputStream fin = new ObjectInputStream(
                    new FileInputStream(name + HISTORY_EXT));

            history = (ArrayList<Message>) fin.readObject();
            fin.close();

        } catch (IOException | ClassNotFoundException ignored) {
        }


        // On enregistre l'arraylist lors de l'arret
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ObjectOutputStream fout = new ObjectOutputStream(
                        new FileOutputStream(name + HISTORY_EXT));

                fout.writeObject(history);
                fout.flush();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        if (this.history == null) {
            this.history = new ArrayList<>();
        }

        this.users = new ArrayList<>();
    }

    /**
     * Adds a user to the room
     *
     * @param client Client handler to be added
     * @return if succeeded
     */
    public boolean join(Client client) {

        users.add(client);

        for (Message message :
                history) {
            client.send(message.toString());
        }

        broadcast(null, String.format("<b>%s</b> has joined the room.", client.getNick()));
        return false;
    }

    /**
     * Broadcasts a message to all users in the room
     *
     * @param emitter Sender of the message
     * @param message Message to be broadcast
     */
    public void broadcast(Client emitter, String message) {
        broadcast(emitter, message, true);
    }

    /**
     * Broadcasts a message to all users in the room with or without notifying the sender
     *
     * @param emitter Sender of the message
     * @param message Message to be broadcast
     * @param notifyEmitter Indicates if the sender should be notified
     */
    public void broadcast(Client emitter, String message, boolean notifyEmitter) {
        Message msg = new Message(message, emitter != null ? emitter.getNick() : null);

        history.add(msg);

        for (Client ct :
                users) {
            if (ct == emitter && notifyEmitter) {
                ct.send(msg.toString());
            }
        }
    }

    /**
     * Removes a client from the room
     *
     * @param client Client leaving the room
     * @return if succeeded
     */
    public boolean leave(Client client) {
        broadcast(null, String.format("<b>%s</b> has left the room.", client.getNick()), false);
        return users.remove(client);
    }
}