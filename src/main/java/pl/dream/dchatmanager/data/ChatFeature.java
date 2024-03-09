package pl.dream.dchatmanager.data;

public class ChatFeature {
    private final boolean enabled;

    public ChatFeature(boolean enabled){
        this.enabled = enabled;
    }

    public boolean isEnabled(){
        return enabled;
    }
}
