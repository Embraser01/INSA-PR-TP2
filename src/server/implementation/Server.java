package server.implementation;

import protocol.IClient;
import protocol.INotifier;
import protocol.IServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class Server implements IServer {

    private static final String ROOM_NAME_REGEX = "^[a-zA-Z0-9_]+$";
    private HashMap<String, Room> rooms;

    public Server() {
        this.rooms = new HashMap<>();
    }

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

    public Room join(Client client, String room) {
        if (!room.matches(ROOM_NAME_REGEX)) return null;

        Room room1 = rooms.computeIfAbsent(room, k -> new Room(room));

        room1.join(client);
        return room1;
    }
}
