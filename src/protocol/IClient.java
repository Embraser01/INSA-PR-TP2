package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by RMI to describe the methods of the client object returned by the server, allowing the client to
 * call methods on the server.
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public interface IClient extends Remote {

    /**
     * Joins a room
     *
     * @param roomId ID of the room to be joined
     * @return if succeeded
     * @throws RemoteException RMI Exception
     */
    boolean join(String roomId) throws RemoteException;

    /**
     * Leaves the current room of the user
     *
     * @throws RemoteException RMI Exception
     */
    void leave() throws RemoteException;

    /**
     * Updates the nickname of the user
     *
     * @param nickname New nickname to be used
     * @return if succeeded
     * @throws RemoteException RMI Exception
     */
    boolean updateNickname(String nickname) throws RemoteException;

    /**
     * Returns the help string from the server
     *
     * @return Help message
     * @throws RemoteException RMI Exception
     */
    String help() throws RemoteException;

    /**
     * Broadcasts a message to the other users in the room
     *
     * @param message Message to be broadcast
     * @return if succeeded
     * @throws RemoteException RMI Exception
     */
    boolean broadcast(String message) throws RemoteException;
}
