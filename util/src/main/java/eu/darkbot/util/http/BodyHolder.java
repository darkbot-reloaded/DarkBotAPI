package eu.darkbot.util.http;

import java.util.Arrays;

class BodyHolder {
    private byte[] body;
    private ParamBuilder paramBuilder;

    private ParamBuilder getParams() {
        if (paramBuilder == null) {
            if (body != null)
                throw new UnsupportedOperationException("Cannot mix body & params");
            paramBuilder = new ParamBuilder();
        }
        return paramBuilder;
    }

    boolean isValid() {
        return paramBuilder != null || body != null;
    }

    boolean hasParams() {
        return paramBuilder != null;
    }

    @SuppressWarnings("PMD.MethodReturnsInternalArray")
    byte[] getBytes() {
        if (body != null) return body;
        if (paramBuilder != null) return paramBuilder.getBytes();

        throw new IllegalStateException("data & params are null!");
    }

    void setParam(Object key, Object value) {
        getParams().set(key, value);
    }

    void setRawParam(Object key, Object value) {
        getParams().setRaw(key, value);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    void setBody(byte[] body) {
        if (paramBuilder != null)
            throw new UnsupportedOperationException("Cannot mix body & params");
        this.body = body;
    }

    @Override
    public String toString() {
        if (paramBuilder != null)
            return paramBuilder.toString();
        if (body != null)
            return Arrays.toString(body);

        return super.toString();
    }
}
