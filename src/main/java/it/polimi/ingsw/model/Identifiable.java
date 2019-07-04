package it.polimi.ingsw.model;

import java.util.UUID;

/**
 * Class for identify each instance of the class with an UUID
 */
public class Identifiable {
    /**
     * The UUID that identifies the instance.
     */
    private String uuid;

    /**
     * Constructor of the class, assign a random UUID to the instance.
     */
    public Identifiable(){
        uuid = UUID.randomUUID().toString();
    }

    /**
     * Constructor of the class, assign a given UUID to the instance.
     * @param uuid
     */
    protected Identifiable(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Return the UUID.
     * @return The UUID.
     */
    public String getUuid(){
        return uuid;
    }
}
