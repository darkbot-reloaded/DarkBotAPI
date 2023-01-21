package eu.darkbot.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;

@UtilityClass
public class SystemUtils {
    /**
     * @param url to open via user's default browser
     * @return if opening the url was successful, false otherwise
     */
    public static boolean openUrl(@NotNull String url) {
        return openUrl(URI.create(url));
    }

    /**
     * @param url to open via user's default browser
     * @return if opening the url was successful, false otherwise
     */
    public static boolean openUrl(@NotNull URI url) {
        try {
            Desktop.getDesktop().browse(url);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param toCopy string to copy into user's clipboard
     * @throws HeadlessException if {@link GraphicsEnvironment#isHeadless()} returns true
     */
    public static void toClipboard(String toCopy) {
        if (toCopy == null || toCopy.isEmpty()) return;
        StringSelection selection = new StringSelection(toCopy);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}
