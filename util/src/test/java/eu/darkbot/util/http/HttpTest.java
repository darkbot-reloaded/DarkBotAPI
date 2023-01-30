package eu.darkbot.util.http;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

class HttpTest {

    @Test
    @Disabled("Avoid calls on every build")
    void testJson() throws IOException {
        TestPojo pojo = new TestPojo(UUID.randomUUID().toString(), Math.random(), new Random().nextInt());

        // echo webhook - may expire at some point
        TestPojo notEncoded = Http.create("https://webhook.site/e271ef28-b332-4388-8348-887f0dbc78c1", Method.POST)
                .setJsonBody(pojo)
                .fromJson(TestPojo.class);

        Assertions.assertEquals(pojo, notEncoded);

        TestPojo encoded = Http.create("https://webhook.site/e271ef28-b332-4388-8348-887f0dbc78c1", Method.POST)
                .setJsonBody(pojo, true)
                .fromJson(TestPojo.class, true);

        Assertions.assertEquals(pojo, encoded);
    }

    @EqualsAndHashCode
    static class TestPojo {
        private final String a;
        private final double b;
        private final int c;

        TestPojo(String a, double b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
