package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.UUID;

public class Identifiable implements Serializable {
    private static final long serialVersionUID = -6497812949531091114L;
    private String uuid;

    public Identifiable(){
        uuid = UUID.randomUUID().toString();
    }
    protected Identifiable(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid(){
        return uuid;
    }
}
