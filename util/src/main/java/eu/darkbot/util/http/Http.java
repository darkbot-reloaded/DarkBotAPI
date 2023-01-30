package eu.darkbot.util.http;

import com.google.gson.Gson;
import eu.darkbot.util.IOUtils;
import eu.darkbot.util.function.ThrowingFunction;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Utility for HTTP connections.
 * Use it like builder, just one time for instance
 */
public class Http {
    private static final Gson GSON = new Gson();

    @Getter @Setter
    private static String defaultUserAgent = "BigpointClient/1.6.7";

    protected final String baseUrl;
    protected final Method method;
    protected final boolean followRedirects;
    protected final BodyHolder bodyHolder = new BodyHolder();

    //Discord doesn't handle java's user agent...
    protected String userAgent = defaultUserAgent;
    protected List<Runnable> suppliers;
    protected Map<String, String> headers = new LinkedHashMap<>();

    protected Http(String baseUrl, Method method, boolean followRedirects) {
        this.baseUrl = baseUrl;
        this.method = method;
        this.followRedirects = followRedirects;
    }

    public static Gson getGson() {
        return GSON;
    }

    /**
     * Creates new instance of Http with provided url.
     * Request method is {@link Method#GET} and follows redirects by default.
     *
     * @param url to connect
     * @return new Http
     */
    public static Http create(String url) {
        return new Http(url, Method.GET, true);
    }

    /**
     * Creates new instance of Http with provided url and request method.
     * Follow redirects by default.
     *
     * @param url    to connect
     * @param method of request
     * @return new Http
     */
    public static Http create(String url, Method method) {
        return new Http(url, method, true);
    }

    /**
     * Creates new instance of Http with provided url and follow redirects.
     * Request method is {@link Method#GET} by default.
     *
     * @param url             to connect
     * @param followRedirects should follow redirects, response code 3xx
     * @return new Http
     */
    public static Http create(String url, boolean followRedirects) {
        return new Http(url, Method.GET, followRedirects);
    }

    /**
     * Creates new instance of Http with provided arguments.
     *
     * @param url             to connect
     * @param method          of request
     * @param followRedirects should follow redirects, response code 3xx
     * @return new Http
     */
    public static Http create(String url, Method method, boolean followRedirects) {
        return new Http(url, method, followRedirects);
    }

    /**
     * Adds action which will be executed at the end of the connection
     *
     * @param action to execute
     * @return current instance of Http
     */
    public Http addSupplier(Runnable action) {
        if (suppliers == null) suppliers = new ArrayList<>();
        this.suppliers.add(action);
        return this;
    }

    /**
     * Sets or overrides connection header.
     * Encoded via {@link java.net.URLEncoder#encode(String, String)} in UTF-8
     * To set header without encoding look {@link Http#setRawHeader(String, String)}
     * <p>
     * Equivalent to {@link HttpURLConnection#setRequestProperty(String, String)}
     *
     * @param key   of header
     * @param value of header
     * @return current instance of Http
     */
    public Http setHeader(String key, String value) {
        this.headers.put(ParamBuilder.encode(key), ParamBuilder.encode(value));
        return this;
    }

    /**
     * Sets or overrides connection header without encoding.
     * Equivalent to {@link HttpURLConnection#setRequestProperty(String, String)}
     *
     * @param key   of header
     * @param value of header
     * @return current instance of Http
     */
    public Http setRawHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * Sets or overrides parameter for POST as body or for GET as
     * additional query url only if current url doesn't contains '?' char.
     * Is encoded via {@link java.net.URLEncoder#encode(String, String)}
     *
     * @param key   of parameter
     * @param value of parameter
     * @return current instance of Http
     */
    public Http setParam(Object key, Object value) {
        this.bodyHolder.setParam(key, value);
        return this;
    }

    /**
     * Sets or overrides parameter for POST as body or for GET as
     * additional query url only if current url doesn't contains '?' char.
     * Be aware, this wont be encoded via {@link java.net.URLEncoder#encode(String, String)}
     *
     * @param key   of parameter
     * @param value of parameter
     * @return current instance of Http
     */
    public Http setRawParam(Object key, Object value) {
        this.bodyHolder.setRawParam(key, value);
        return this;
    }

    /**
     * Set the body for POST requests
     *
     * @param body bytes to send as body
     * @return current instance of http
     */
    public Http setBody(byte[] body) {
        this.bodyHolder.setBody(body);
        return this;
    }

    /**
     * Serializes the object into JSON and set it as POST body
     *
     * @param json object to be serialized into JSON
     * @return current instance of http
     */
    public Http setJsonBody(Object json) throws IOException {
        return setJsonBody(json, false);
    }

    /**
     * Serializes the object into JSON and set it as POST body
     *
     * @param json         object to be serialized into JSON
     * @param encodeBase64 should JSON be encoded in Base64
     * @return current instance of http
     */
    public Http setJsonBody(Object json, boolean encodeBase64) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (OutputStream out = encodeBase64 ? Base64.getEncoder().wrap(baos) : baos;
             OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            GSON.toJson(json, osw);
        }

        return setBody(baos.toByteArray());
    }

    /**
     * Sets user agent used in connection.
     *
     * @param userAgent to use.
     * @return current instance of Http
     */
    public Http setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public URL getUrl() throws IOException {
        String url = baseUrl;
        if (method == Method.GET && bodyHolder.hasParams())
            url += (url.contains("?") ? "" : "?") + bodyHolder;

        return new URL(url);
    }

    /**
     * Connects, gets and converts InputStream to String then closes stream.
     * <b>Creates new connection on each call</b>
     *
     * @return body of request as String
     * @throws IOException of {@link IOUtils#read(InputStream)}
     */
    public String getContent() throws IOException {
        return IOUtils.read(getInputStream(), true);
    }

    /**
     * Deserializes the JSON response from the input stream into an object of the specified type
     *
     * @param type class type to be deserialized to
     * @return deserialized JSON response
     */
    public <T> T fromJson(Class<T> type) throws IOException {
        return fromJson(type, false);
    }

    /**
     * Deserializes the JSON response from the input stream into an object of the specified type
     *
     * @param type     class type to be deserialized to
     * @param isBase64 is JSON response base64 encoded
     * @return deserialized JSON response
     */
    public <T> T fromJson(Class<T> type, boolean isBase64) throws IOException {
        return fromJson((Type) type, isBase64);
    }

    /**
     * Deserializes the JSON response from the input stream into an object of the specified type
     *
     * @param type     type to be deserialized to
     * @param isBase64 is JSON response base64 encoded
     * @return deserialized JSON response
     */
    public <T> T fromJson(Type type, boolean isBase64) throws IOException {
        try (InputStream in = isBase64 ? Base64.getDecoder().wrap(getInputStream()) : getInputStream();
             InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, type);
        }
    }

    /**
     * Gets and closes InputStream of current connection.
     * <b>Creates new connection on each call</b>
     *
     * @throws IOException of {@link Http#getInputStream()}
     */
    public void closeInputStream() throws IOException {
        getInputStream().close();
    }

    /**
     * Gets InputStream of current connection.
     * <b>Creates new connection on each call</b>
     *
     * @return InputStream of connection
     * @throws IOException of {@link Http#getConnection()}
     */
    public InputStream getInputStream() throws IOException {
        return getConnection().getInputStream();
    }

    /**
     * Gets InputStream of current connection and applies consumer then closes InputStream.
     * <b>Creates new connection on each call</b>
     *
     * <pre>{@code
     *         try {
     *             String result = Http.create("https://example.com")
     *                     .consumeInputStream(IOUtils::read);
     *         } catch (IOException e) {
     *             System.out.println("Something went wrong");
     *         }
     * }</pre>
     *
     * @param function function which will consume InputStream
     * @param <R>      type of return
     * @param <X>      type of exception
     * @return the result of calling function with the input stream
     * @throws X if your function throws an exception
     */
    @SuppressWarnings({"unchecked", "PMD.AvoidCatchingThrowable"})
    public <R, X extends Throwable> R consumeInputStream(ThrowingFunction<InputStream, R, X> function) throws X, IOException {
        try (InputStream is = getInputStream()) {
            try {
                return function.apply(is);
            } catch (Throwable t) {
                throw (X) t;
            }
        }
    }

    /**
     * Gets {@link HttpURLConnection} with provided params,
     * request method, and body.
     * <b>Creates new connection on each call</b>
     *
     * @return HttpURLConnection
     * @throws IOException of connection
     */
    public HttpURLConnection getConnection() throws IOException {
        return getConnection(null);
    }

    /**
     * Gets {@link HttpURLConnection} with provided params,
     * request method, and body.
     * <b>Creates new connection on each call</b>
     *
     * @param customSettings custom settings of connection which they
     *                       will be consumed after initializing http.
     * @return HttpURLConnection
     * @throws IOException of connection
     */
    public HttpURLConnection getConnection(Consumer<HttpURLConnection> customSettings) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) getUrl().openConnection();
        conn.setConnectTimeout(30_000);
        conn.setReadTimeout(30_000);
        if (customSettings != null) customSettings.accept(conn);

        conn.setInstanceFollowRedirects(followRedirects);
        conn.setRequestProperty("User-Agent", userAgent);
        if (!headers.isEmpty()) headers.forEach(conn::setRequestProperty);

        if (method == Method.POST && bodyHolder.isValid()) {
            conn.setDoOutput(true);
            byte[] data = bodyHolder.getBytes();
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data);
            }
        }
        if (suppliers != null) suppliers.forEach(Runnable::run);

        return conn;
    }

}
