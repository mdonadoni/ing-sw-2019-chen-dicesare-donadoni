package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.RemoteServer;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a local view.
 */
public abstract class LocalView implements View, Runnable {

    private static final Logger LOG = Logger.getLogger(LocalView.class.getName());


    private Server server = null;
    private ConnectionType connType = null;
    private Thread socketThread = null;
    private Socket socket = null;

    protected LocalView() {
    }

    /**
     * Connect to the server using RMI.
     * @param address Address of the server.
     * @param port Port of the RMI registry on the server.
     * @throws RemoteException If there is a network error.
     */
    protected void connectServerRMI(String address, int port) throws IOException {
        UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.getRegistry(address, port);
        try {
            server = (Server) registry.lookup("Server");
        } catch(NotBoundException e) {
            throw new IOException("Server not bound to registry", e);
        }
        connType = ConnectionType.RMI;
    }

    /**
     * Connect to the server using a socket.
     * @param address Address of the server.
     * @param port Port of the socket server.
     * @throws IOException If there is an error while making the connection.
     */
    protected void connectServerSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
        RemoteServer remote = new RemoteServer(this, socket);
        server = remote;
        socketThread = new Thread(remote);
        socketThread.start();
        connType = ConnectionType.SOCKET;
    }

    /** Method to get a reference to the remote server. This method can be
     * called only after creating a successful connection either using RMI
     * or sockets.
     * @return Remote server.
     */
    protected Server getServer() {
        return server;
    }

    /**
     * Method to start the local view.
     */
    public abstract void run();

    /**
     * Show a message. This method doesn't throw RemoteException because this
     * is a local view.
     * @param message Massage to be shown.
     */
    @Override
    public abstract void showMessage(String message);

    /**
     * Signal a disconnection from the server. This method doesn't throw RemoteException because this
     * is a local view.
     */
    @Override
    public void disconnect() {
        switch (connType) {
            case RMI:
                try {
                    UnicastRemoteObject.unexportObject(this, true);
                    LOG.info("Unexported remote object");
                } catch (RemoteException e) {
                    LOG.log(Level.SEVERE, "Cannot unexport remote object", e);
                }
                break;
            case SOCKET:
                socketThread.interrupt();
                LOG.info("Thread interrupted");
                try {
                    socket.close();
                    LOG.info("Socket closed");
                } catch(IOException e) {
                    LOG.log(Level.SEVERE, "Cannot close socket", e);
                }
        }
    }
}
