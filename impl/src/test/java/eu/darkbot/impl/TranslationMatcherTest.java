package eu.darkbot.impl;

import eu.darkbot.api.managers.GameResourcesAPI;
import eu.darkbot.impl.utils.TranslationMatcherImpl;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

@VisibleForTesting
class TranslationMatcherTest {

    @Test
    void patternTest() {
        String log = "%CLAN% is now in control of the %STATION% Battle Station on the %MAP% map!" +
                " This Battle Station's Defelctor is being activated.";

        String clan = "#497 18623 WHATEVER CLAN  &.73!@#$!$^)#@$*@% ";
        String station = "REALLY WEIRD s.,][][1\3@$^@i0gfua/sXSD!@#% Station  ";
        String map = "I s th ..XX+T@_$TT%_C@#_P{}#$P}{{}{{][] 2-3 a map?";

        GameResourcesAPI.TranslationMatcher translationMatcher =  // 1, 2, 3
                new TranslationMatcherImpl(Locale.ENGLISH, log, "%MAP%", "%CLAN%", "%STATION%");

        String logToProcess = log.replace("%CLAN%", clan)
                .replace("%STATION%", station)
                .replace("%MAP%", map);

        Assertions.assertTrue(translationMatcher.find(logToProcess));

        Assertions.assertEquals(map, translationMatcher.get(1));
        Assertions.assertEquals(clan, translationMatcher.get(2));
        Assertions.assertEquals(station, translationMatcher.get(3));
    }
}
