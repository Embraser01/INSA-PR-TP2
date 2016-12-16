package server.implementation;

import protocol.IClient;
import protocol.INotifier;
import protocol.IServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Server object used by clients to obtain a client handler. Exposed via the RMI Registry
 */
public class Server implements IServer {

    /**
     * Regex of allowed room names
     */
    private static final String ROOM_NAME_REGEX = "^[a-zA-Z0-9_]+$";

    /**
     * Room list
     */
    private HashMap<String, Room> rooms;

    /**
     * Initialization constructor
     */
    public Server() {
        this.rooms = new HashMap<>();
    }

    /**
     * Method creating a client connection by returning a client handler
     *
     * @param notifier Notifier object of the client
     * @param nickname Nickname of the client
     * @return
     */
    @Override
    public IClient connect(INotifier notifier, String nickname) {
        try {
            return (IClient) UnicastRemoteObject.exportObject(
                    new Client(this, notifier, nickname),
                    0);
        } catch (RemoteException e) {
            System.out.println("Server error : ");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Makes a client join a room
     *
     * @param client Client joining a room
     * @param room Room to be joined
     * @return
     */
    public Room join(Client client, String room) {
        if (!room.matches(ROOM_NAME_REGEX)) return null;

        Room room1 = rooms.computeIfAbsent(room, k -> new Room(room));

        room1.join(client);
        return room1;
    }
}
