package pl.dream.dchatmanager.data;

public class AntiSpam extends ChatFeature {

    //Cooldown in seconds * 20 ticks
    private final int cooldown;

    public AntiSpam(boolean enabled, int cooldown) {
        super(enabled);

        this.cooldown = cooldown * 20;
    }

    public int getCooldown(){
        return cooldown;
    }
}
