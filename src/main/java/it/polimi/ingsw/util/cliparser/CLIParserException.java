package it.polimi.ingsw.util.cliparser;

/**
 * Exception used by the parser of CLI arguments.
 */
public class CLIParserException extends RuntimeException {
    /**
     * Create a new CLIParserException with given message.
     * @param message Message describing the exception.
     */
    CLIParserException(String message) {
        super(message);
    }
}
