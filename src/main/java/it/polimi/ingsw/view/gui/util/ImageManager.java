package it.polimi.ingsw.view.gui.util;

import it.polimi.ingsw.util.ResourceManager;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static final String PATH_RED_MARK = "/gui/red_mark.png";

    private static Map<String, Image> imageMap = new HashMap<>();
    private static Map<String, Image> customImageMap = new HashMap<>();

    public static Image getResourceImage(String path) {
        if (!imageMap.containsKey(path)) {
            imageMap.put(path, new Image(ResourceManager.get(path)));
        }
        return imageMap.get(path);
    }

    public static Image getCustomImage(String key) {
        return customImageMap.get(key);
    }

    public static void addCustomImage(String key, Image img) {
        customImageMap.put(key, img);
    }

    public static boolean hasCustomImage(String key) {
        return customImageMap.containsKey(key);
    }

    public static Image getRedMark() {
        return getResourceImage(PATH_RED_MARK);
    }
}
