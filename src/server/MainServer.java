package server;

import protocol.IServer;
import server.implementation.Server;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main Server class, used to setup and expose the Server object, allowing users to connect to the chat
 *
 * @author Tristan Bourvon
 * @author Marc-Antoine FERNANDES
 * @version 1.0.0
 */
public class MainServer {

    /**
     * Main entry point of the program, setting up the server
     *
     * @param args Arguments given to the program, args[0] is the port on which to listen
     */
    public static void main(String args[]) {

        Integer port = (args.length < 1) ? 1099 : Integer.parseInt(args[0]);
        try {

            Registry registry = LocateRegistry.createRegistry(port);

            Server obj = new Server();
            IServer stub = (IServer) UnicastRemoteObject.exportObject(obj, 0);

            registry.bind("Server", stub);

            System.err.println("Server ready");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
