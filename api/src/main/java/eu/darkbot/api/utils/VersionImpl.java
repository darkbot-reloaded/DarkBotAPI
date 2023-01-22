package eu.darkbot.api.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@JsonAdapter(VersionImpl.Adapter.class)
public class VersionImpl implements Version {
    protected static final Pattern VERSION = Pattern.compile(
            "(?<major>[^0-9]*[0-9]+)" +
            "(\\.(?<minor>[0-9]+))?" +
            "(\\.(?<patch>[0-9]+))?" +
            "(\\.(?<revision>[0-9]+))?" +
            "(?<beta> ?b(eta)? ?(?<betanum>[0-9]+)?)?" +
            "(?<alpha> ?a(lpha)? ?(?<alphanum>[0-9]+)?)?");

    protected final String version;

    protected final int major;
    protected final int minor;
    protected final int patch;
    protected final int revision;
    protected final int beta;
    protected final int alpha;

    protected VersionImpl(String version) {
        Matcher matcher = VERSION.matcher(version);
        if (!matcher.matches()) throw new IllegalArgumentException("Couldn't parse version " + version);
        this.major = Integer.parseInt(matcher.group("major"));
        this.minor = getInt(matcher, "minor");
        this.patch = getInt(matcher, "patch");
        this.revision = getInt(matcher, "revision");

        int beta = getOptionalInt(matcher, "beta");
        int alpha = getOptionalInt(matcher, "alpha");
        this.beta = beta == -1 && alpha == -1 ? Integer.MAX_VALUE : beta;
        this.alpha = alpha == -1 ? Integer.MAX_VALUE : alpha;
        this.version = getVersionString();
    }

    protected VersionImpl(int major, int minor, int patch) {
        this(major, minor, patch, -1, -1, -1);
    }

    protected VersionImpl(int major, int minor, int patch, int revision, int beta, int alpha) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.revision = revision;
        this.beta = beta == -1 && alpha == -1 ? Integer.MAX_VALUE : beta;
        this.alpha = alpha == -1 ? Integer.MAX_VALUE : alpha;
        this.version = getVersionString();
    }

    private static int getInt(Matcher m, String groupName) {
        String result = m.group(groupName);
        return result == null ? -1 : Integer.parseInt(result);
    }

    private static int getOptionalInt(Matcher m, String groupName) {
        String result = m.group(groupName);
        if (result == null) return -1;
        String num = m.group(groupName + "num");
        return num == null ? 0 : Integer.parseInt(num);
    }

    @Override
    public boolean isBeta() {
        return beta != Integer.MAX_VALUE;
    }

    @Override
    public boolean isAlpha() {
        return alpha != Integer.MAX_VALUE;
    }

    private String getVersionString() {
        StringBuilder versionBuilder = new StringBuilder(String.valueOf(major));

        if (minor != -1) versionBuilder.append('.').append(minor);
        if (patch != -1) versionBuilder.append('.').append(patch);
        if (revision != -1) versionBuilder.append('.').append(revision);

        if (beta != Integer.MAX_VALUE) versionBuilder.append(" b").append(beta);
        if (alpha != Integer.MAX_VALUE) versionBuilder.append(" a").append(alpha);

        return versionBuilder.toString();
    }

    @Override
    public String toString() {
        return version;
    }

    public static class Adapter extends TypeAdapter<Version> {
        @Override
        public void write(JsonWriter writer, Version value) throws IOException {
            writer.value(value.toString());
        }

        @Override
        public Version read(JsonReader in) throws IOException {
            return new VersionImpl(in.nextString());
        }
    }
}
