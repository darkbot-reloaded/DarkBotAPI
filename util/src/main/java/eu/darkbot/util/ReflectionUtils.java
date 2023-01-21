package eu.darkbot.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@UtilityClass
public class ReflectionUtils {

    /** A map from primitive types to their corresponding wrapper types. */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Map.of(
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            char.class, Character.class,
            double.class, Double.class,
            float.class, Float.class,
            int.class, Integer.class,
            long.class, Long.class,
            short.class, Short.class,
            void.class, Void.class);

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrapped(Class<T> type) {
        if (!type.isPrimitive()) return type;
        return (Class<T>) PRIMITIVE_TO_WRAPPER.get(type);
    }

    public static <T> T get(Field field, Object obj, Class<T> clazz) {
        try {
            return clazz.cast(field.get(obj));
        } catch (ClassCastException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void set(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Type[] findGenericParameters(Class<?> clazz, Class<?> generic) {
        Type[] params;
        for (Type itf : clazz.getGenericInterfaces()) {
            params = getTypes(itf, generic);
            if (params != null) return params;
            if (itf instanceof Class) {
                params = findGenericParameters((Class<?>) itf, generic);
                if (params != null) return params;
            }
        }
        params = getTypes(clazz.getGenericSuperclass(), generic);
        if (params != null) return params;

        Class<?> parent = clazz.getSuperclass();
        if (parent != null) return findGenericParameters(parent, generic);
        return null;
    }

    public static Type[] getTypes(Type type, Class<?> expected) {
        if (!(type instanceof ParameterizedType)) return null;
        ParameterizedType paramType = (ParameterizedType) type;
        if (paramType.getRawType() == expected) return paramType.getActualTypeArguments();
        return null;
    }

}
