package it.polimi.ingsw.model.minified;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class MiniIdentifiable implements Serializable {
    private static final long serialVersionUID = 5562137633697751083L;
    private String uuid;

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
