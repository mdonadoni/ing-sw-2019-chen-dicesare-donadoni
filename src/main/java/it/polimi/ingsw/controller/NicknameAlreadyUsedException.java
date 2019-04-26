package it.polimi.ingsw.controller;

/**
 * Exception thrown when a user chooses an already used nickname.
 */
public class NicknameAlreadyUsedException extends Exception {
    /**
     * Constructor of NicknameAlreadyUsedException.
     */
    public NicknameAlreadyUsedException() {
        super();
    }

    /**
     * Constructor of NicknameAlreadyUsedException.
     * @param message Description of exception.
     */
    public NicknameAlreadyUsedException(String message) {
        super(message);
    }
}
