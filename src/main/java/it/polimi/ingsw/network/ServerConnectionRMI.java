package it.polimi.ingsw.network;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Connection with the server using RMI.
 */
public class ServerConnectionRMI implements ServerConnection {
    /**
     * Remote server.
     */
    private Server server;

    /**
     * Local view that is exported.
     */
    private LocalView view;

    /**
     * Constructs the connection and connects with the server.
     * @param address Address of the server.
     * @param port Port of the RMI registry.
     * @param view View to be exported.
     * @throws IOException If the connection cannot be made.
     */
    public ServerConnectionRMI(String address, int port, LocalView view) throws IOException{
        this.view = view;
        Registry registry = LocateRegistry.getRegistry(address, port);
        try {
            server = (Server) registry.lookup("Server");
        } catch(NotBoundException e) {
            throw new IOException("Server not bound to registry", e);
        }
        UnicastRemoteObject.exportObject(view, 0);
    }

    /**
     * Get remote server.
     * @return Remote Server.
     */
    @Override
    public Server getServer() {
        return server;
    }

    /**
     * Close the connection by unexporting the view.
     * @throws IOException If the connection cannot be interrupted.
     */
    @Override
    public void close() throws IOException {
        UnicastRemoteObject.unexportObject(view, true);
    }
}
