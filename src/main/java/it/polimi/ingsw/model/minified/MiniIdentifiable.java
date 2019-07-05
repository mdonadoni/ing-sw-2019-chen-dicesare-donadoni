package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

/**
 * Identifiable for the client side
 */
public class MiniIdentifiable implements Serializable {
    /**
     * Serializable UID
     */
    private static final long serialVersionUID = 5562137633697751083L;
    /**
     * UUID
     */
    private String uuid;

    /**
     * Constructor of the class
     * @param uuid The UUID to set
     */
    public MiniIdentifiable(String uuid) {
        this.uuid = uuid;
    }

    @JsonCreator
    MiniIdentifiable() {

    }

    public String getUuid() {
        return uuid;
    }
}
