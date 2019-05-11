package it.polimi.ingsw.network.socket.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.network.socket.messages.server.LoginRequest;
import it.polimi.ingsw.network.socket.messages.server.LoginResponse;
import it.polimi.ingsw.network.socket.messages.view.*;

import java.util.UUID;

/**
 * Generic socket message. Every message has a defining uuid.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginRequest.class, name = "LoginRequest"),
        @JsonSubTypes.Type(value = LoginResponse.class, name = "LoginResponse"),
        @JsonSubTypes.Type(value = DisconnectRequest.class, name = "DisconnectRequest"),
        @JsonSubTypes.Type(value = SelectObjectRequest.class, name = "SelectObjectRequest"),
        @JsonSubTypes.Type(value = SelectObjectResponse.class, name = "SelectObjectResponse"),
        @JsonSubTypes.Type(value = ShowMessageRequest.class, name = "ShowMessageRequest"),
        @JsonSubTypes.Type(value = VoidResponse.class, name = "VoidResponse"),
})
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
