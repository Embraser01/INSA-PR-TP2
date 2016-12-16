package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Used by RMI to describe the object used by the server to notify clients of new incoming messages
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public interface INotifier extends Remote {

    /**
     * Notifies the client of a new message
     *
     * @param message New message
     * @throws RemoteException RMI Exception
     */
    void notifyMessage(String message) throws RemoteException;
}
