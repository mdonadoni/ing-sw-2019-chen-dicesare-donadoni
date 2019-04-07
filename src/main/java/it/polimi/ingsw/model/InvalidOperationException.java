package it.polimi.ingsw.model;

/**
 * Exception that signals an invalid operation
 */
public class InvalidOperationException extends RuntimeException {
    /**
     * Constructor of InvalidOperationException
     */
    public InvalidOperationException() {
        super();
    }

    /**
     * Constructor of InvalidOperationException that accepts a message.
     * @param message Message that describes the exception.
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}