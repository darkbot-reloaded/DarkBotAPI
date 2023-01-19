package eu.darkbot.api.config.types;

public interface ZoneInfo {

    int getResolution();

    void setResolution(int resolution);

    boolean get(int x, int y);

    void set(int x, int y);

    void remove(int x, int y);

    void toggle(int x, int y);

    default void set(int x, int y, int x2, int y2) {
        for (; x < x2; x++) {
            for (int currY = y; currY < y2; currY++) {
                set(x, currY);
            }
        }
    }

    default void remove(int x, int y, int x2, int y2) {
        for (; x < x2; x++) {
            for (int currY = y; currY < y2; currY++) {
                remove(x, currY);
            }
        }
    }

    default void toggle(int x, int y, int x2, int y2) {
        for (; x < x2; x++) {
            for (int currY = y; currY < y2; currY++) {
                toggle(x, currY);
            }
        }
    }

    default void set(int x, int y, int x2, int y2, boolean state) {
        if (state) {
            set(x, y, x2, y2);
        } else {
            remove(x, y, x2, y2);
        }
    }

    default boolean outside(int x, int y) {
        return x < 0 || y < 0 || x >= getResolution() || y >= getResolution();
    }

    default boolean contains(double xPercent, double yPercent) {
        return get(Math.min((int) (xPercent * getResolution()), getResolution() - 1),
                Math.min((int) (yPercent * getResolution()), getResolution() - 1));
    }

}
