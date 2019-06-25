package it.polimi.ingsw.util.config;

public class ConfigException extends RuntimeException {
    ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    ConfigException(String message) {
        super(message);
    }
}
