package it.polimi.ingsw.network.socket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic Json Socket endpoint. This class is thread-safe.
 * @param <I> Type of incoming messages.
 * @param <O> Type of outgoing messages.
 */
public class SocketEndpoint<I, O> {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SocketEndpoint.class.getName());

    /**
     * Socket used to read/write.
     */
    private final Socket socket;

    /**
     * Class of incoming messages. This is needed because Java generics are not
     * powerful enough.
     */
    private final Class<I> inClass;

    /**
     * BufferedReader constructed on top of socket.
     */
    private final BufferedReader reader;

    /**
     * PrintWriter constructed on top of socket.
     */
    private final BufferedWriter writer;

    /**
     * Jackson's ObjectMapper used to serialize/deserialize objects into/from Json.
     * This object is static because it's very expensive.
     */
    private static final ObjectMapper jsonMapper;
    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        jsonMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Constructor for SocketEndpoint.
     * @param socket Socket to be used.
     * @param inClass Class of incoming messages.
     * @throws IOException If there is an error constructing the stream needed.
     */
    public SocketEndpoint(Socket socket, Class<I> inClass) throws IOException {
        this.socket = socket;
        this.inClass = inClass;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Send a message.
     * @param msg Message to be sent.
     * @throws IOException If there are errors while sending the message.
     */
    public void send(O msg) throws IOException {
        LOG.log(Level.INFO, "Sending {0}", msg);
        synchronized (writer) {
            String json = jsonMapper.writeValueAsString(msg);
            LOG.info(() -> "Sending JSON: " + json);
            writer.write(json);
            writer.newLine();
            writer.flush();
        }
        LOG.info("Message to view sent");
    }

    /**
     * Read a message from the socket.
     * @return Message read from socket.
     * @throws IOException If there are errors while reading the message.
     */
    public I receive() throws IOException {
        synchronized (reader) {
            String msg = reader.readLine();
            if (msg == null) {
                throw new IOException("Nothing to read");
            }
            return jsonMapper.readValue(msg, inClass);
        }
    }

}
