package it.polimi.ingsw.util.config;

/**
 * Exception for configuration
 */
public class ConfigException extends RuntimeException {
    ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    ConfigException(String message) {
        super(message);
    }
}
