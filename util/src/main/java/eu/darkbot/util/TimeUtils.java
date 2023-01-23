package eu.darkbot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {
    public static final long
            SECOND = 1000,
            MINUTE = SECOND * 60,
            HOUR = MINUTE * 60,
            DAY = HOUR * 24;

    /**
     * Will try to sleep the thread for specified period of time(ms)
     *
     * @param time time in milliseconds to sleep
     */
    public static void sleepThread(long time) {
        if (time <= 0) return;

        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }
}
