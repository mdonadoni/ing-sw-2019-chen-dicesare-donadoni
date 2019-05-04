package it.polimi.ingsw.network.socket.messages;

import java.util.UUID;

/**
 * Generic socket message. Every message has a defining uuid.
 */
public class Message {
    /**
     * uuid of this message.
     */
    String uuid;

    /**
     * Constructor of a Message.
     * @param uuid uuid of this message.
     */
    protected Message(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Constructor of a Message with random uuid.
     */
    protected Message() {
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * Get the uuid of this message.
     * @return String containing the uuid.
     */
    public String getUUID() {
        return uuid;
    }


    @Override
    public String toString() {
        return "[" + getUUID() + "] " + this.getClass().getSimpleName();
    }
}
