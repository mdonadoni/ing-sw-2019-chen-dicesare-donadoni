package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for void methods.
 */
public class VoidResponse extends ResponseViewMethod {
    /**
     * Constructor of a VoidResponse.
     * @param req Request corresponding to this response.
     */
    VoidResponse(RequestViewMethod req) {
        this(req.getUUID());
    }

    /**
     * Private constructor for VoidResponse.
     * @param uuid UUID of response.
     */
    @JsonCreator
    private VoidResponse(@JsonProperty("uuid") String uuid) {
        super(uuid);
    }
}
