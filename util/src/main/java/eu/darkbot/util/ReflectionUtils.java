package eu.darkbot.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    private ReflectionUtils() {}

    /** A map from primitive types to their corresponding wrapper types. */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
    static {
        Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap<>(16);
        primitiveToWrapper.put(boolean.class, Boolean.class);
        primitiveToWrapper.put(byte.class, Byte.class);
        primitiveToWrapper.put(char.class, Character.class);
        primitiveToWrapper.put(double.class, Double.class);
        primitiveToWrapper.put(float.class, Float.class);
        primitiveToWrapper.put(int.class, Integer.class);
        primitiveToWrapper.put(long.class, Long.class);
        primitiveToWrapper.put(short.class, Short.class);
        primitiveToWrapper.put(void.class, Void.class);

        PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMap(primitiveToWrapper);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrapped(Class<T> type) {
        if (!type.isPrimitive()) return type;
        return (Class<T>) PRIMITIVE_TO_WRAPPER.get(type);
    }

    public static <T> T get(Field field, Object obj, Class<T> clazz) {
        try {
            return clazz.cast(field.get(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void set(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Type[] findGenericParameters(Class<?> clazz, Class<?> generic) {
        Type[] params;
        for (Type itf : clazz.getGenericInterfaces()) {
            if ((params = getTypes(itf, generic)) != null) return params;
            if (itf instanceof Class) {
                params = findGenericParameters((Class<?>) itf, generic);
                if (params != null) return params;
            }
        }
        if ((params = getTypes(clazz.getGenericSuperclass(), generic)) != null) return params;

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
