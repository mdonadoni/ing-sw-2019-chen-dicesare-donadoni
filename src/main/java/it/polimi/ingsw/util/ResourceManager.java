package it.polimi.ingsw.util;

import java.io.InputStream;

public class ResourceManager {
    public static InputStream get(String path) {
        InputStream stream = ResourceManager.class.getResourceAsStream(path);
        if (stream == null) {
            throw new ResourceException("Cannot find resource " + path);
        }
        return stream;
    }
}
