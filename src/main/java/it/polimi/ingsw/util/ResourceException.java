package it.polimi.ingsw.util;

/**
 * Exception that signals an error while loading resources.
 */
public class ResourceException extends RuntimeException {
    /**
     * Constructor of ResourceException.
     * @param message Message of the exception.
     * @param t Cause of the exception.
     */
    public ResourceException(String message, Throwable t) {
        super(message, t);
    }

    public ResourceException(String message) {
        super(message);
    }
}
