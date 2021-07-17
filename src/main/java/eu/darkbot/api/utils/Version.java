package eu.darkbot.api.utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Version extends Comparable<Version> {

    static Version of(String version) {
        return new VersionImpl(version);
    }

    int getMajor();

    int getMinor();

    int getPatch();

    int getRevision();

    boolean isBeta();
    int getBeta();

    boolean isAlpha();
    int getAlpha();

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

    class VersionImpl implements Version {
        private static final Pattern VERSION = Pattern.compile("" +
                "([^0-9]*[0-9]+)" + // Major
                "(\\.([0-9]+))?" + // Minor
                "(\\.([0-9]+))?" + // Patch
                "(\\.([0-9]+))?" + // Revision
                "( ?beta ?([0-9]+)?)?" + // Beta
                "( ?alpha ?([0-9]+)?)?"); // Alpha

        private final String version;
        private final int major, minor, patch, revision, beta, alpha;

        public int getMajor() {
            return major;
        }

        public int getMinor() {
            return minor;
        }

        public int getPatch() {
            return patch;
        }

        public int getRevision() {
            return revision;
        }

        public boolean isBeta() {
            return beta != Integer.MAX_VALUE;
        }

        public int getBeta() {
            return beta;
        }

        public boolean isAlpha() {
            return alpha != Integer.MAX_VALUE;
        }

        public int getAlpha() {
            return alpha;
        }

        public VersionImpl(String version) {
            this.version = version;
            Matcher matcher = VERSION.matcher(version);
            if (!matcher.matches()) throw new IllegalArgumentException("Couldn't parse version " + version);
            major = Integer.parseInt(matcher.group(1));
            minor = getInt(matcher, 2);
            patch = getInt(matcher, 4);
            revision = getInt(matcher, 6);

            int tmpBeta = getInt(matcher, 8);
            int tmpAlpha = getInt(matcher, 10);
            beta = tmpBeta == -1 && tmpAlpha == -1 ? Integer.MAX_VALUE : tmpBeta;
            alpha = tmpAlpha == -1 ? Integer.MAX_VALUE : tmpAlpha;
        }

        public VersionImpl(int major, int minor, int patch) {
            this(major, minor, patch, -1, -1, -1);
        }

        public VersionImpl(int major, int minor, int patch, int revision, int beta, int alpha) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.revision = revision;

            this.beta = beta == -1 && alpha == -1 ? Integer.MAX_VALUE : beta;
            this.alpha = alpha == -1 ? Integer.MAX_VALUE : alpha;
            this.version = major + "." + minor + "." + patch + "." + revision + " beta " + beta + " alpha " + alpha;
        }

        private int getInt(Matcher m, int find) {
            if (m.group(find) == null) return -1;
            String num = m.group(find + 1);
            return num == null ? 0 : Integer.parseInt(m.group(find + 1));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Version)) return false;

            Version v = (Version) o;

            if (getMajor() != v.getMajor()) return false;
            if (getMinor() != v.getMinor()) return false;
            if (getPatch() != v.getPatch()) return false;
            if (getRevision() != v.getRevision()) return false;
            if (isBeta() != v.isBeta()) return false;
            return isAlpha() == v.isAlpha();
        }

        @Override
        public int hashCode() {
            return Objects.hash(major, minor, patch, revision, beta, alpha);
        }

        @Override
        public String toString() {
            return version;
        }

    }

}
