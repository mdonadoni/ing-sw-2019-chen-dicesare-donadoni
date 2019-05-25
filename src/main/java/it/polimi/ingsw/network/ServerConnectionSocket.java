package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.RemoteServer;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection to the server using sockets.
 */
public class ServerConnectionSocket implements ServerConnection {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ServerConnectionSocket.class.getName());
    /**
     * Thread for socket operations.
     */
    private Thread threadSocket;
    /**
     * Remote server.
     */
    private RemoteServer server;
    /**
     * Network socket used by this connection.
     */
    private Socket socket;

    /**
     * Connect to the server using sockets.
     * @param address Address of the server.
     * @param port Port of the server.
     * @param view View that is attached to this connection.
     * @throws IOException If the connection cannot be made.
     */
    public ServerConnectionSocket(String address, int port, LocalView view) throws IOException {
        socket = new Socket(address, port);
        try {
            server = new RemoteServer(view, socket);
        } catch (Exception e) {
            try {
                socket.close();
            } catch (Exception se) {
                LOG.log(Level.SEVERE, "Couldn't close the socket", se);
            }
            throw e;
        }
        threadSocket = new Thread(server);
        threadSocket.start();
    }

    /**
     * Get remote server.
     * @return Remote server.
     */
    @Override
    public Server getServer() {
        return server;
    }

    /**
     * Close the connection.
     * @throws IOException If the connection cannot be closed.
     */
    @Override
    public void close() throws IOException {
        threadSocket.interrupt();
        server.close();
    }
}
