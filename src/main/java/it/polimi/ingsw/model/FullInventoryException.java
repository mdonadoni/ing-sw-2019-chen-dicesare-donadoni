package it.polimi.ingsw.model;

/**
 * Exception for when the inventory is full.
 */
public class FullInventoryException extends InvalidOperationException {
    /**
     * Standard Constructor
     */
    public FullInventoryException(){
        super();
    }

    /**
     * Constructor with a message
     * @param message The message
     */
    public FullInventoryException(String message){
        super(message);
    }
}
