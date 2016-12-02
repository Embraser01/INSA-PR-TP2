package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by marca on 02/12/2016.
 */
public interface IClient extends Remote {

    boolean join(String roomId) throws RemoteException;

    void leave() throws RemoteException;

    boolean updateNickname(String nickname) throws RemoteException;

    String help() throws RemoteException;

    boolean broadcast(String message) throws RemoteException;
}
