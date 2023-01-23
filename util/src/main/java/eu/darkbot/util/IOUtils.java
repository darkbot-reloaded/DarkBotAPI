package eu.darkbot.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class IOUtils {

    public static void write(OutputStream output, String str) throws IOException {
        output.write(str.getBytes());
    }

    public static String read(InputStream input) throws IOException {
        return read(input, false);
    }

    public static String read(InputStream input, boolean closeStream) throws IOException {
        return new String(readByteArray(input, closeStream), StandardCharsets.UTF_8);
    }

    public static byte[] readByteArray(InputStream input, boolean closeStream) throws IOException {
        if (closeStream) {
            try (input) {
                return input.readAllBytes();
            }
        }
        return input.readAllBytes();
    }
}
