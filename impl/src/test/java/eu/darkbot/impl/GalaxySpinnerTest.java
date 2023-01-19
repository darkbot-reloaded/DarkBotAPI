package eu.darkbot.impl;

import eu.darkbot.api.game.galaxy.GalaxyGate;
import eu.darkbot.api.managers.BackpageAPI;
import eu.darkbot.api.managers.EventBrokerAPI;
import eu.darkbot.api.managers.GalaxySpinnerAPI;
import eu.darkbot.impl.managers.GalaxySpinner;
import eu.darkbot.util.http.Http;
import eu.darkbot.util.http.Method;
import org.jetbrains.annotations.VisibleForTesting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import static org.mockito.Mockito.*;

@VisibleForTesting
class GalaxySpinnerTest {

    @Test
    void testSpin() throws IOException {
        Http http = mock(Http.class, withSettings()
                .useConstructor("https://localhost/flashinput/galaxyGates.php", Method.GET, true)
                .defaultAnswer(Answers.CALLS_REAL_METHODS));
        doReturn(new ByteArrayInputStream("materializer locked".getBytes()))
                .when(http).getInputStream();

        BackpageAPI api = mock(BackpageAPI.class);
        doReturn(Instant.now()).when(api).getLastRequestTime();
        doReturn(true).when(api).isInstanceValid();
        doReturn(1234).when(api).getUserId();
        doReturn("123456789").when(api).getSid();
        doReturn(http).when(api).getHttp(anyString(), anyInt());

        GalaxySpinnerAPI spinner = new GalaxySpinner(api, mock(EventBrokerAPI.class));

        spinner.spinGate(GalaxyGate.ALPHA, true, 1, 0);

        Assertions.assertEquals(new URL("https://localhost/flashinput/galaxyGates.php?" +
                "userID=1234&action=multiEnergy&sid=123456789&gateID=1&alpha=1&multiplier=1"),
                http.getUrl());
    }

}
