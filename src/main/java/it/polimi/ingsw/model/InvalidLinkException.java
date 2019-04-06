package it.polimi.ingsw.model;

/**
 * Exception that signals an invalid Link
 */
public class InvalidLinkException extends RuntimeException {
    /**
     * Constructor of InvalidLinkException
     */
    public InvalidLinkException() {
        super();
    }

    /**
     * Constructor of InvalidLinkException that accepts a message.
     * @param message Message that describes the exception.
     */
    public InvalidLinkException(String message) {
        super(message);
    }
}
