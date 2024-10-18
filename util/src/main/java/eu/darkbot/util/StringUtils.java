package eu.darkbot.util;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {
    private static final Pattern NON_CHAR_REPLACEMENT = Pattern.compile("[^A-Za-z0-9]");
    private static final Pattern MIMESIS_REPLACEMENT = Pattern.compile("m[i1]m[e3][s5][i1][s5]");

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String fuzzyNpcName(String name) {
        // xX[ Chaos Protegit ]Xx or -x-[ Synk ]-x-, make sure to remove the "x"
        if ((name.startsWith("xX[") && name.endsWith("]Xx"))
                || (name.startsWith("-x-") && name.endsWith("-x-"))) {
            name = name.substring(3, name.length() - 3);
        }
        // Special case, keep upper & lower case
        if (!name.contains("StreuneR")) {
            name = name.toLowerCase(Locale.ROOT);
        }
        // Fix up referee bot
        name = name.replace("referee binary bot", "referee bot");
        // Fix up mimesis
        name = MIMESIS_REPLACEMENT.matcher(name).replaceAll("mimesis");

        // Keep only alphanumerical chars
        return NON_CHAR_REPLACEMENT.matcher(name).replaceAll("");
    }
}
