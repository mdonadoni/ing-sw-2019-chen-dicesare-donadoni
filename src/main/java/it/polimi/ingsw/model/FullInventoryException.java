package it.polimi.ingsw.model;

/**
 * Exception for when the inventory is full.
 */
public class FullInventoryException extends InvalidOperationException {
    public FullInventoryException(){
        super();
    }

    public FullInventoryException(String message){
        super(message);
    }
}
