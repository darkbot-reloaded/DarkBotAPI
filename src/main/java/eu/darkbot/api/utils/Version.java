package eu.darkbot.api.utils;

public interface Version extends Comparable<Version> {

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

}
