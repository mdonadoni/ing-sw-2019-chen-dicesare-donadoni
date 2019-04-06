package it.polimi.ingsw.model;

/**
 * Exception that signals an invalid Square
 */
public class InvalidSquareException extends RuntimeException {
    /**
     * Constructor of InvalidSquareException
     */
    public InvalidSquareException() {
        super();
    }

    /**
     * Constructor of InvalidSquareException that accepts a message.
     * @param message Message that describes the exception.
     */
    public InvalidSquareException(String message) {
        super(message);
    }
}
