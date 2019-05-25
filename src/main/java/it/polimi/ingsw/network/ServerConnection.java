package it.polimi.ingsw.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for a connection with a server.
 */
public interface ServerConnection extends Closeable {
    /**
     * Get reference to remote server.
     * @return Remote server.
     */
    Server getServer();

    /**
     * Close the connection.
     * @throws IOException If the connection cannot be closed.
     */
    void close() throws IOException;
}
