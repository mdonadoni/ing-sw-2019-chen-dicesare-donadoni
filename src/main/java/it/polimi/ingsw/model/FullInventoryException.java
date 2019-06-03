package it.polimi.ingsw.model;

public class FullInventoryException extends InvalidOperationException {
    public FullInventoryException(){
        super();
    }

    public FullInventoryException(String message){
        super(message);
    }
}
