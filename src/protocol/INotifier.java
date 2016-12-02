package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by marca on 02/12/2016.
 */
public interface INotifier extends Remote {

    void notifyMessage(String message) throws RemoteException;
}
