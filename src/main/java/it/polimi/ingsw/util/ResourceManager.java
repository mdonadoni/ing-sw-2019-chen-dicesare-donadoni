package it.polimi.ingsw.util;

import java.io.InputStream;

/**
 * This class represents the resource manager that return a InputStream from the path of a file.
 */
public class ResourceManager {
    /**
     * Return the InputStream of a file.
     * @param path The path of a file.
     * @return The InputStream of the file.
     */
    public static InputStream get(String path) {
        InputStream stream = ResourceManager.class.getResourceAsStream(path);
        if (stream == null) {
            throw new ResourceException("Cannot find resource " + path);
        }
        return stream;
    }
}
