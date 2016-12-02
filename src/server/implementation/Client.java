package server.implementation;

import protocol.IClient;
import protocol.INotifier;

import java.rmi.RemoteException;

public class Client implements IClient {

    private Server server = null;
    private INotifier notifier = null;
    private Room room = null;
    private String nick = null;


    public Client(Server server, INotifier notifier, String nick) {
        this.server = server;
        this.notifier = notifier;
        this.nick = nick;
    }

    @Override
    public boolean join(String roomId) {
        if (nick == null) return false;
        if (room != null) room.leave(this);
        room = server.join(this, roomId);
        return true;
    }

    @Override
    public void leave() {
        if (room != null) room.leave(this);
        room = null;
    }

    @Override
    public boolean updateNickname(String nickname) {
        nick = nickname;
        // TODO Check if not exists
        return true;
    }

    @Override
    public String help() {
        return "<html>Help :<br>" +
                "&#09;/join &lt;room_id&gt; : Join a room (if absent, a new one will be created)<br>" +
                "&#09;/leave : Return to the lobby<br>" +
                "&#09;/nick : Change your nickname<br>" +
                "&#09;/help : Display this menu</html>";
    }

    @Override
    public boolean broadcast(String message) {
        if (room == null) return false;

        room.broadcast(this, message);
        return true;
    }

    public void send(String message) {
        try {
            notifier.notifyMessage(message);
        } catch (RemoteException e) {
            System.out.println("Client error : ");
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}
