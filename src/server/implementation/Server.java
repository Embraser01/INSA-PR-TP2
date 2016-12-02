package server.implementation;

import protocol.IClient;
import protocol.INotifier;
import protocol.IServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class Server implements IServer {

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
        Room room1 = rooms.computeIfAbsent(room, k -> new Room());

        room1.join(client);
        return room1;
    }
}
