package it.polimi.ingsw.model;

import java.util.*;

public class Util {
    /**
     * This class should not be constructed.
     */
    private Util() {}

    /**
     * Given a list, this method returns a new list composed only of unique
     * values from the given list, sorted by the (decreasing) number of
     * occurrences of that value in the original list. If two values occur the
     * same number of times, the original order is preserved.
     * @param list List to be used.
     * @param <T> Type of the objects inside the list.
     * @return List of unique values sorted by decreasing number of occurrences.
     */
    public static <T> List<T> uniqueStableSortByCount(List<T> list) {
        List<T> unique = new ArrayList<>();
        Map<T, Integer> count = new HashMap<>();

        for (T obj : list) {
            if (!count.containsKey(obj)) {
                unique.add(obj);
            }
            count.put(obj, count.getOrDefault(obj, 0)+1);
        }

        unique.sort(Comparator.comparingInt(count::get).reversed());
        return unique;
    }
}
