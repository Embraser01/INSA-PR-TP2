package server.implementation;

import java.io.*;
import java.util.ArrayList;

public class Room {

    private static final String HISTORY_EXT = ".history";

    private ArrayList<Message> history = null;
    private ArrayList<Client> users = null;
    private String name = null;

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

    public boolean join(Client client) {

        users.add(client);

        for (Message message :
                history) {
            client.send(message.toString());
        }

        broadcast(null, String.format("<b>%s</b> has joined the room.", client.getNick()));
        return false;
    }

    public void broadcast(Client emitter, String message) {
        broadcast(emitter, message, true);
    }

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

    public boolean leave(Client client) {
        broadcast(null, String.format("<b>%s</b> has left the room.", client.getNick()), false);
        return users.remove(client);
    }
}