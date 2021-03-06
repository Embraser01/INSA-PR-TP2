package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Describes the method of the Server object, used to get a new client handle upon connection
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public interface IServer extends Remote {

    /**
     * Connect a client
     * @param notifier message listener
     * @param nickname nickname
     * @return Client Handler
     * @throws RemoteException RMI Exception
     */
    IClient connect(INotifier notifier, String nickname) throws RemoteException;
}
