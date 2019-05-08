package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response of selectSquares.
 */
public class SelectObjectResponse extends ResponseViewMethod {
    /**
     * List of squares' coordinates chosen.
     */
    private List<String> res;

    /**
     * Constructor of SelectObjectResponse.
     * @param uuid UUID of corresponding request.
     * @param res Result of selectSquares.
     */
    @JsonCreator
    private SelectObjectResponse(@JsonProperty("uuid") String uuid,
                                 @JsonProperty("res") List<String> res) {
        super(uuid);
        this.res = res;
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
     * Get selectSquares result.
     * @return List of squares' coordinates chosen.
     */
    public List<String> getResult() {
        return res;
    }
}
