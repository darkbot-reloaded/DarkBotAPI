package eu.darkbot.impl.decorators;

public interface ClassDecorator<T> {

    Class<T> handledType();

    default void tryLoad(Object obj) {
        Class<T> cls = handledType();
        if (!cls.isInstance(obj)) return;
        load(cls.cast(obj));
    }

    void load(T t);

    default void tryUnload(Object obj) {
        Class<T> cls = handledType();
        if (!cls.isInstance(obj)) return;
        unload(cls.cast(obj));
    }

    void unload(T t);

}
