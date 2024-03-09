package pl.dream.dchatmanager.data.feature;

public class ChatFeature {
    private boolean enabled;

    public ChatFeature(boolean enabled){
        this.enabled = enabled;
    }

    public boolean isEnabled(){
        return enabled;
    }
    public void enable(boolean value){
        enabled = value;
    }
}
