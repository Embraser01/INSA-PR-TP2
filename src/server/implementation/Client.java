package server.implementation;

import protocol.IClient;
import protocol.INotifier;

import java.rmi.RemoteException;

/**
 * Client handler given to the client to call methods on the server to join, leave, etc
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class Client implements IClient {

    /**
     * Server object used to interact with rooms and other users
     */
    private Server server = null;

    /**
     * Notifier object used to notify the client of new messages
     */
    private INotifier notifier = null;

    /**
     * The current room of the user
     */
    private Room room = null;

    /**
     * Nickname of the user
     */
    private String nick = null;

    /**
     * Initialization constructor, also sends a welcome message
     *
     * @param server see {@link #server}
     * @param notifier see {@link #notifier}
     * @param nick see {@link #nick}
     */
    public Client(Server server, INotifier notifier, String nick) {
        this.server = server;
        this.notifier = notifier;
        this.nick = nick;

        send(String.format("<html><span>Welcome %s!<br>" +
                "To get help type : /help<span></html>", nick));
    }

    /**
     * Joins the room with ID roomId
     *
     * @param roomId ID of the room to join
     * @return if succeeded
     */
    @Override
    public boolean join(String roomId) {
        if (nick == null) return false;
        if (room != null) room.leave(this);
        room = server.join(this, roomId);
        return room != null;
    }

    /**
     * Leave the current room of the user
     */
    @Override
    public void leave() {
        if (room != null) room.leave(this);
        room = null;
    }

    /**
     * Updates the nickname of the user
     *
     * @param nickname New nickname
     * @return if succeeded
     */
    @Override
    public boolean updateNickname(String nickname) {
        nick = nickname;
        // TODO Check if not exists
        return true;
    }

    /**
     * Return the help string explaining how to user chat commands
     *
     * @return if succeeded
     */
    @Override
    public String help() {
        return "<html>Help :<br>" +
                "&#09;/join &lt;room_id&gt; : Join a room (if absent, a new one will be created)<br>" +
                "&#09;/leave : Return to the lobby<br>" +
                "&#09;/nick : Change your nickname<br>" +
                "&#09;/help : Display this menu</html>";
    }

    /**
     * Broadcasts a message to all other users in the room
     *
     * @param message Message to be broadcast
     * @return if succeeded
     */
    @Override
    public boolean broadcast(String message) {
        if (room == null) return false;

        room.broadcast(this, message);
        return true;
    }

    /**
     * Used by the server to send a message to the client
     *
     * @param message Message to be sent
     */
    public void send(String message) {
        try {
            notifier.notifyMessage(message);
        } catch (RemoteException e) {
            System.out.println("Client error : ");
            e.printStackTrace();
            if (room != null) room.leave(this);
        }
    }

    /**
     * Nickname getter
     *
     * @return nickname
     */
    public String getNick() {
        return nick;
    }
}
