package eu.darkbot.api.utils;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(VersionImpl.Adapter.class)
public interface Version extends Comparable<Version> {

    /**
     * Creates {@code Version} from the string
     * ex: "1.2.3 b 1 a 2", "3.2.1 beta 2 alpha 1"
     *
     * @param version string to be parsed
     * @return {@code Version} representation of the given string
     * @throws IllegalArgumentException if {@code version} doesn't match Version pattern
     * @since 0.7.0
     */
    static Version of(String version) {
        return new VersionImpl(version);
    }

    static Version of(int major, int minor, int patch) {
        return new VersionImpl(major, minor, patch);
    }

    static Version of(int major, int minor, int patch, int revision, int beta, int alpha) {
        return new VersionImpl(major, minor, patch, revision, beta, alpha);
    }

    int getMajor();

    int getMinor();

    int getPatch();

    int getRevision();

    boolean isBeta();
    int getBeta();

    boolean isAlpha();
    int getAlpha();

    default boolean isSameAs(Version other) {
        return compareTo(other) == 0;
    }

    default boolean isNewerThan(Version other) {
        return compareTo(other) > 0;
    }

    default boolean isOlderThan(Version other) {
        return compareTo(other) < 0;
    }

    @Override
    default int compareTo(Version o) {
        if (getMajor() != o.getMajor()) return Integer.compare(getMajor(), o.getMajor());
        if (getMinor() != o.getMinor()) return Integer.compare(getMinor(), o.getMinor());
        if (getPatch() != o.getPatch()) return Integer.compare(getPatch(), o.getPatch());
        if (getRevision() != o.getRevision()) return Integer.compare(getRevision(), o.getRevision());
        if (getBeta() != o.getBeta()) return Integer.compare(getBeta(), o.getBeta());
        if (getAlpha() != o.getAlpha()) return Integer.compare(getAlpha(), o.getAlpha());
        return 0;
    }

}
