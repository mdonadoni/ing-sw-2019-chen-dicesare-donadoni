package it.polimi.ingsw.util;

import java.util.HashMap;
import java.util.Map;

public class BlockingMap<K, V> {
    private final Map<K, V> map = new HashMap<>();

    public synchronized V get(K key) throws InterruptedException {
        while(!map.containsKey(key)) {
            wait();
        }
        return map.get(key);
    }

    public synchronized void put(K key, V value) {
        map.put(key, value);
        notifyAll();
    }

    public synchronized V getAndRemove(K key) throws InterruptedException {
        V res = get(key);
        map.remove(key);
        return res;
    }
}
