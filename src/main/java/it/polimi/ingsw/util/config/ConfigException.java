package it.polimi.ingsw.util.config;

/**
 * Exception for configuration
 */
public class ConfigException extends RuntimeException {
    /**
     * Constructor of ConfigException given message and cause.
     * @param message Message of exception.
     * @param cause Cause of exception.
     */
    ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of ConfigException given message.
     * @param message Message of this exception.
     */
    ConfigException(String message) {
        super(message);
    }
}
