package eu.darkbot.api.utils;

public class ItemNotEquippedException extends Exception {
    public static final long serialVersionUID = 1L;

    public ItemNotEquippedException(EquippableItem item) {
        super("Item " + item.getName() + " was not equipped");
    }

    public ItemNotEquippedException(EquippableItem item, String fallback) {
        super("Item " + (item == null ?  fallback : item.getName()) + " was not equipped");
    }
}
