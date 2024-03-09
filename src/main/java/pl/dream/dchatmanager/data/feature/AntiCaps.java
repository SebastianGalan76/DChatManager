package pl.dream.dchatmanager.data.feature;

public class AntiCaps extends ChatFeature{
    private final int minMessageLength;
    private final int maxCapsPercent;

    public AntiCaps(boolean enabled, int minMessageLength, int maxCapsPercent) {
        super(enabled);

        this.minMessageLength = minMessageLength;
        this.maxCapsPercent = maxCapsPercent;
    }

    public int getMinMessageLength() {
        return minMessageLength;
    }

    public int getMaxCapsPercent() {
        return maxCapsPercent;
    }
}
