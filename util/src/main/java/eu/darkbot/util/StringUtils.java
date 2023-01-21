package eu.darkbot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
