package eu.darkbot.util.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class HttpTest {

    @Test
    public void testJson() throws IOException {
        Http.setGson(new Gson());
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

    public static class TestPojo {
        private final String a;
        private final double b;
        private final int c;

        public TestPojo(String a, double b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestPojo pojo = (TestPojo) o;

            if (Double.compare(pojo.b, b) != 0) return false;
            if (c != pojo.c) return false;
            return Objects.equals(a, pojo.a);
        }
    }
}
