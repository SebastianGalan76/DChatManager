package pl.dream.dchatmanager.data.feature;

public class AntiSpam extends ChatFeature {

    //Cooldown in milliseconds
    private final int cooldown;

    public AntiSpam(boolean enabled, int cooldown) {
        super(enabled);

        this.cooldown = cooldown * 1000;
    }

    public int getCooldown(){
        return cooldown;
    }
}
