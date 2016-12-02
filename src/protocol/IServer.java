package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by marca on 02/12/2016.
 */
public interface IServer extends Remote {

    IClient connect(INotifier notifier, String nickname) throws RemoteException;
}
