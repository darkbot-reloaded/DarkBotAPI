package eu.darkbot.util;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    @SafeVarargs
    public static <T> List<T> asImmutableList(T... a) {
        return Collections.unmodifiableList(Arrays.asList(a));
    }

    /**
     * Returns true whenever collection is not null and not empty.
     */
    public static boolean isNotBlank(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }

    public static <T> List<T> mergeLists(List<T> l1, List<T> l2) {
        l1.addAll(l2);
        return l1;
    }

    @SafeVarargs
    public static <T> List<T> enumValuesAsList(Class<? extends T>... enumTypes) {
        return Arrays.stream(enumTypes)
                .map(Class::getEnumConstants)
                .flatMap(Arrays::stream)
                .map(e -> (T) e)
                .collect(Collectors.toList());
    }
}
