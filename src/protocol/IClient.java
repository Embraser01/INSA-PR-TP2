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
     * @return
     * @throws RemoteException
     */
    boolean join(String roomId) throws RemoteException;

    /**
     * Leaves the current room of the user
     *
     * @throws RemoteException
     */
    void leave() throws RemoteException;

    /**
     * Updates the nickname of the user
     *
     * @param nickname New nickname to be used
     * @return
     * @throws RemoteException
     */
    boolean updateNickname(String nickname) throws RemoteException;

    /**
     * Returns the help string from the server
     *
     * @return
     * @throws RemoteException
     */
    String help() throws RemoteException;

    /**
     * Broadcasts a message to the other users in the room
     *
     * @param message Message to be broadcast
     * @return
     * @throws RemoteException
     */
    boolean broadcast(String message) throws RemoteException;
}
