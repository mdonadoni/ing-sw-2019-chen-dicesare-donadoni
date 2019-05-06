package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Coordinate;

import java.util.List;

/**
 * Response of selectSquares.
 */
public class SelectSquaresResponse extends ResponseViewMethod {
    /**
     * List of squares' coordinates chosen.
     */
    private List<Coordinate> res;

    /**
     * Constructor of SelectSquaresResponse.
     * @param uuid UUID of corresponding request.
     * @param res Result of selectSquares.
     */
    @JsonCreator
    private SelectSquaresResponse(@JsonProperty("uuid") String uuid,
                                  @JsonProperty("res") List<Coordinate> res) {
        super(uuid);
        this.res = res;
    }

    /**
     * Constructor of SelectSquaresResponse.
     * @param req Corresponding request.
     * @param res Result of selectSquares.
     */
    SelectSquaresResponse(SelectSquaresRequest req, List<Coordinate> res) {
        this(req.getUUID(), res);
    }

    /**
     * Get selectSquares result.
     * @return List of squares' coordinates chosen.
     */
    public List<Coordinate> getResult() {
        return res;
    }
}
