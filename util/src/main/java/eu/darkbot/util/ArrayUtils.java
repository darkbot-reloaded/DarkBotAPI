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

}
