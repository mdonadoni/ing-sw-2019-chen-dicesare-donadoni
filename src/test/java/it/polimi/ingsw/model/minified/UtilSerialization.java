package it.polimi.ingsw.model.minified;

import it.polimi.ingsw.util.Json;

import java.io.*;

public class UtilSerialization {
    public static <T> T jackson(T obj, Class<T> clazz) throws IOException {
        String json = Json.getMapper().writeValueAsString(obj);
        return Json.getMapper().readValue(json, clazz);
    }

    public static <T> T javaSerializable(T obj, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        out.flush();
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return clazz.cast(in.readObject());
    }
}
