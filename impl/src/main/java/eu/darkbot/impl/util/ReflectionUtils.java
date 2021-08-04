package eu.darkbot.impl.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {
    private ReflectionUtils() {}

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
