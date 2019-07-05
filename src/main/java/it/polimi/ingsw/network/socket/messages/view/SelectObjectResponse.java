package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.network.socket.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Response of selectSquares.
 */
public class SelectObjectResponse extends Message {
    /**
     * List of Object chosen.
     */
    private ArrayList<String> res;

    /**
     * Constructor of SelectObjectResponse.
     * @param uuid UUID of corresponding request.
     * @param res Result of selectObjects.
     */
    @JsonCreator
    private SelectObjectResponse(@JsonProperty("uuid") String uuid,
                                 @JsonProperty("res") List<String> res) {
        super(uuid);
        this.res = new ArrayList<>(res);
    }

    /**
     * Constructor of SelectObjectResponse.
     * @param req Corresponding request.
     * @param res Result of selectSquares.
     */
    SelectObjectResponse(SelectObjectRequest req, List<String> res) {
        this(req.getUUID(), res);
    }

    /**
     * Get selectOjbect result.
     * @return List of ojbect.
     */
    public ArrayList<String> getResult() {
        return res;
    }
}
