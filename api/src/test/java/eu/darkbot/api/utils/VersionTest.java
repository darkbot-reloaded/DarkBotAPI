package eu.darkbot.api.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VersionTest {

    @Test
    void testVersion() {
        Version ver1 = Version.of("1.1.1 alpha 1");
        Version ver2 = Version.of("1.1.1 alpha 2");
        Version ver3 = Version.of("2.1.1 alpha 2");
        Version ver4 = Version.of("2.1.1 alpha 2");
        Version ver5 = Version.of("2.1.1 b1 alpha 2");
        Version ver6 = Version.of("2.1.1 b1");
        Version ver7 = Version.of("1.118 beta 3 a1");

        Assertions.assertTrue(ver2.isNewerThan(ver1));
        Assertions.assertTrue(ver3.isNewerThan(ver1));
        Assertions.assertTrue(ver5.isNewerThan(ver4));
        Assertions.assertTrue(ver4.isSameAs(ver3));
        Assertions.assertTrue(ver6.isNewerThan(ver5));
        Assertions.assertTrue(ver7.isOlderThan(ver5));

        Assertions.assertFalse(ver1.isNewerThan(ver4));
    }
}
