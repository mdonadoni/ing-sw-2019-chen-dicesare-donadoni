package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.LocalServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener of a ServerSocket waiting for new connections from remote views.
 */
public class SocketListener implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SocketListener.class.getName());

    /**
     * Reference to local server.
     */
    LocalServer server;

    /**
     * ServerSocket to be listened.
     */
    ServerSocket serverSocket;

    /**
     * Constructor of ServerListener.
     * @param server Local server.
     * @param serverSocket ServerSocket to be listened.
     */
    public SocketListener(LocalServer server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    /**
     * When running, the SocketListener waits for new connections and then
     * makes a new RemoteView that listens on another thread.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new RemoteView(server, socket)).start();
                LOG.info("New socket connection accepted");
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Couldn't accept new socket view", e);
            }
        }
    }
}
