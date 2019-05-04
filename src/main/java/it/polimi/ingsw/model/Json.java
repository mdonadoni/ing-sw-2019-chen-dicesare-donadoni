package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class used to store a single ObjectMapper of Jackson.
 */
public class Json {
    private static ObjectMapper mapper;

    /**
     * Singleton. ObjectMapper is a very expensive object, so we only use one.
     * @return ObjectMapper
     */
    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
        return mapper;
    }
}
