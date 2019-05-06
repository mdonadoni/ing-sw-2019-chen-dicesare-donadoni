package it.polimi.ingsw.network.socket.messages.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.network.LocalView;

import java.util.List;

/**
 * Request to select squares to the view
 */
public class SelectSquaresRequest extends RequestViewMethod {
    /**
     * List of squares' coordinates.
     */
    private List<Coordinate> squares;

    /**
     * Minimum number of squares to be chosen.
     */
    private int min;

    /**
     * Maximum number of squares to be chosen.
     */
    private int max;

    /**
     * Constructor of a SelectSquaresRequest.
     * @param squares List of squares' coordinates.
     * @param min Minimum number of squares to be chosen.
     * @param max Maximum number of squares to be chosen.
     */
    @JsonCreator
    public SelectSquaresRequest(@JsonProperty("squares") List<Coordinate> squares,
                                @JsonProperty("min") int min,
                                @JsonProperty("max") int max) {
        this.squares = squares;
        this.min = min;
        this.max = max;
    }

    /**
     * Command Pattern.
     * @param view Method is invoked on this view
     * @return Response to method invocation.
     */
    @Override
    public ResponseViewMethod invokeOn(LocalView view) {
        return new SelectSquaresResponse(this, view.selectSquares(squares, min, max));
    }
}
