package eu.darkbot.impl.decorators;

import eu.darkbot.impl.util.ReflectionUtils;

import java.lang.reflect.Type;

public abstract class AbstractDecorator<T> implements ClassDecorator<T> {

    private final Class<T> handledType;

    public AbstractDecorator() {
        Type[] types = ReflectionUtils.findGenericParameters(getClass(), AbstractDecorator.class);
        if (types == null)
            throw new UnsupportedOperationException("Can't initialize feature decorator with no found type: " + getClass().getCanonicalName());

        //noinspection unchecked
        handledType = (Class<T>) types[0];
    }

    @Override
    public Class<T> handledType() {
        return handledType;
    }

}
