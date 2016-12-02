package server.implementation;

import java.util.ArrayList;

public class Room {

    private ArrayList<String> history = null;
    private ArrayList<Client> users = null;

    public Room() {
        this.history = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public boolean join(Client client) {

        users.add(client);

        for (String message :
                history) {
            client.send(message);
        }

        broadcast(null, String.format("<html><b>%s</b> has joined the room.</html>", client.getNick()));
        return false;
    }

    public void broadcast(Client emitter, String message) {

        String sender = "<html><b>" + (emitter == null ? "Server" : emitter.getNick()) + "</b>: " + message + "</html>";
        history.add(sender);

        for (Client ct :
                users) {
            ct.send(sender);
        }
    }

    public boolean leave(Client client) {
        broadcast(null, String.format("<html><b>%s</b> has left the room.</html>", client.getNick()));
        return users.remove(client);
    }
}