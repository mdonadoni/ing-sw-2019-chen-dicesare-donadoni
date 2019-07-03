package it.polimi.ingsw.model;

import java.util.UUID;

public class Identifiable {
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
