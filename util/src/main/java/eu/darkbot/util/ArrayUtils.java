package eu.darkbot.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ArrayUtils {

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    @SafeVarargs
    public static <T> List<T> asImmutableList(T... a) {
        return Collections.unmodifiableList(Arrays.asList(a));
    }

    /**
     * Returns true whenever collection is not null and not empty.
     * @param collection The collection to check
     * @return true if collection is not null and not empty
     */
    public static boolean isNotBlank(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

}
