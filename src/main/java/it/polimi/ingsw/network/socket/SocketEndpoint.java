package it.polimi.ingsw.network.socket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.network.socket.messages.Message;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic Json Socket endpoint. This class is thread-safe.
 * This class provides a mechanism to send a request and wait for the
 * corresponding response. When a new request is read from the socket (a
 * request is a message which is not a response) a callback is called.
 * @param <RequestIn> Type of incoming request message.
 */
public class SocketEndpoint<RequestIn extends Message> implements Closeable, Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(SocketEndpoint.class.getName());

    /**
     * Message used to gracefully close the endpoint (and the underlying socket).
     * Every string that is not valid Json and doesn't have newlines can be used
     * as STOP_MESSAGE.
     */
    private static final String STOP_MESSAGE = "Forza Atalanta!";

    /**
     * Socket used to read/write.
     */
    private final Socket socket;

    /**
     * Class of incoming messages. This is needed because Java generics are not
     * powerful enough.
     */
    private final Class<RequestIn> requestInClass;

    /**
     * BufferedReader constructed on top of socket.
     */
    private final BufferedReader reader;

    /**
     * BufferedWriter constructed on top of socket.
     */
    private final BufferedWriter writer;

    /**
     * AtomicBoolean that indicates if the endpoint is connected or not.
     */
    private final AtomicBoolean connected;

    /**
     * This map maps the UUID of a response with the corresponding response.
     */
    private final Map<String, Message> responses;

    /**
     * This set maintains the list of the requests' UUID waiting for a response.
     * This set is synchronized.
     */
    private final Set<String> requestUUID;

    /**
     * Callback to call when a new incoming request is read.
     */
    private final Consumer<RequestIn> notifyNewRequest;

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
     * @param requestInClass Class of incoming requests.
     * @param notifyNewRequest Callback to call when new request is read.
     * @throws IOException If there is an error constructing the stream needed.
     */
    public SocketEndpoint(Socket socket, Class<RequestIn> requestInClass, Consumer<RequestIn> notifyNewRequest) throws IOException {
        this.socket = socket;
        this.requestInClass = requestInClass;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.connected = new AtomicBoolean(true);
        this.responses = new HashMap<>();
        this.requestUUID = Collections.synchronizedSet(new HashSet<>());
        this.notifyNewRequest = notifyNewRequest;
    }

    /**
     * Send a message.
     * @param msg Message to be sent.
     * @throws IOException If there are errors while sending the message.
     */
    public void send(Message msg) throws IOException {
        if (!connected.get()) {
            throw new IOException("Endpoint not connected");
        }
        String json = jsonMapper.writeValueAsString(msg);
        LOG.fine(() -> "Sending JSON: " + json);
        synchronized (writer) {
            writer.write(json);
            writer.newLine();
            writer.flush();
        }
        LOG.fine("Message to other endpoint sent");
    }

    /**
     * Send a request and wait for the corresponding response. A response is an
     * incoming message with the same UUID of this request.
     * @param req Request to be sent
     * @param outClass Class of incoming response.
     * @param <T> Type of incoming response
     * @return Response corresponding to given request.
     * @throws RemoteException If something goes wrong.
     */
    public <T extends Message> T sendAndWaitResponse(Message req, Class<T> outClass) throws RemoteException {
        requestUUID.add(req.getUUID());
        try {
            send(req);

            T res;
            synchronized (responses) {
                while (connected.get() && !responses.containsKey(req.getUUID())) {
                    responses.wait();
                }
                if (responses.containsKey(req.getUUID())) {
                   res = outClass.cast(responses.get(req.getUUID()));
                } else {
                    safeClose();
                    throw new RemoteException("Couldn't get response");
                }
            }

            requestUUID.remove(req.getUUID());
            return res;
        } catch (InterruptedException e) {
            safeClose();
            Thread.currentThread().interrupt();
            throw new RemoteException("Request interrupted", e);
        } catch (IOException e) {
            safeClose();
            throw new RemoteException("Couldn't send request", e);
        } catch (ClassCastException e) {
            safeClose();
            throw new RemoteException("Response is not " + outClass.getSimpleName(), e);
        }
    }

    /**
     * Read a message from the socket. If the message is STOP_MESSAGE the
     * endpoint is closed.
     * @return Message read from socket.
     * @throws IOException If there are errors while reading the message.
     */
    public Message receive() throws IOException {
        String msg;
        synchronized (reader) {
            msg = reader.readLine();
        }
        if (msg == null) {
            throw new IOException("Nothing to read");
        }
        if (msg.equals(STOP_MESSAGE)) {
            LOG.info("Endpoint disconnected");
            safeClose();
            throw new IOException("Endpoint disconnected");
        }
        return jsonMapper.readValue(msg, Message.class);
    }

    /**
     * CLose the underlying socket and set the connected state to false.
     * @throws IOException If there are errors while closing the socket.
     */
    @Override
    public void close() throws IOException {
        connected.set(false);
        socket.close();
        synchronized (responses) {
            responses.notifyAll();
        }
    }

    /**
     * Close the socket without throwing.
     */
    private void safeClose() {
        try {
            close();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Couldn't close socket");
        }
    }

    /**
     * Gracefully disconnect the endpoint.
     * @throws IOException If there is an error while disconnecting.
     */
    public void disconnect() throws IOException {
        if (connected.get()) {
            connected.set(false);
            synchronized (writer) {
                writer.write(STOP_MESSAGE);
                writer.newLine();
                writer.flush();
            }
            synchronized (responses) {
                responses.notifyAll();
            }
        }
    }

    /**
     * Listen the socket for incoming requests and responses.
     */
    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        while (connected.get()) {
            try {
                Message msg = receive();
                if (requestUUID.contains(msg.getUUID())) {
                    synchronized (responses) {
                        responses.put(msg.getUUID(), msg);
                        responses.notifyAll();
                    }
                } else {
                    executor.execute(() -> notifyNewRequest.accept(requestInClass.cast(msg)));
                }
            } catch (IOException e) {
                if (connected.get()) {
                    LOG.log(Level.WARNING, "IO error while reading socket", e);
                }
                safeClose();
            } catch (ClassCastException e) {
                LOG.log(Level.WARNING, "Invalid request from socket, disconnecting", e);
                safeClose();
            }
        }
        executor.shutdown();
        LOG.info("Endpoint main loop stopped");
    }
}
