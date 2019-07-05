package it.polimi.ingsw.view.gui.util;

import it.polimi.ingsw.util.ResourceManager;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager of the image
 */
public class ImageManager {
    /**
     * Mark for when a player disconnect during a match
     */
    private static final String PATH_RED_MARK = "/gui/red_mark.png";
    /**
     * Map the image
     */
    private static Map<String, Image> imageMap = new HashMap<>();
    /**
     * Map the custom image
     */
    private static Map<String, Image> customImageMap = new HashMap<>();

    /**
     * This class should not be constructed.
     */
    private ImageManager() {}

    /**
     * Get resource of the image
     * @param path the image path
     * @return the image
     */
    public static Image getResourceImage(String path) {
        if (!imageMap.containsKey(path)) {
            imageMap.put(path, new Image(ResourceManager.get(path)));
        }
        return imageMap.get(path);
    }

    /**
     * Get the custom image
     * @param key the key of the image
     * @return the image
     */
    public static Image getCustomImage(String key) {
        return customImageMap.get(key);
    }

    /**
     * Add custom image
     * @param key the key of the image
     * @param img the image to add
     */
    public static void addCustomImage(String key, Image img) {
        customImageMap.put(key, img);
    }

    /**
     * Return if it a the custom image
     * @param key the key of the image to look for
     * @return if it has a custom image
     */
    public static boolean hasCustomImage(String key) {
        return customImageMap.containsKey(key);
    }

    /**
     * Get image with red mark
     * @return the image with red mark
     */
    public static Image getRedMark() {
        return getResourceImage(PATH_RED_MARK);
    }
}
