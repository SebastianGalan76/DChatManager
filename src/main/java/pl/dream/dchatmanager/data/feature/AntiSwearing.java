package pl.dream.dchatmanager.data.feature;

import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.DChatManager;

import java.util.Set;

public class AntiSwearing extends ChatFeature{

    private final Set<String> blockedWords;

    public AntiSwearing(boolean enabled, Set<String> blockedWords) {
        super(enabled);

        this.blockedWords = blockedWords;
    }

    public boolean addBlockedWord(@NotNull String word){
        if(blockedWords.add(word)){
            DChatManager.getPlugin().configController.reloadBlockedWord();

            return true;
        }

        return false;
    }

    public boolean removeBlockedWord(@NotNull String word){
        if(blockedWords.remove(word)){
            DChatManager.getPlugin().configController.reloadBlockedWord();

            return true;
        }

        return false;
    }

    public boolean checkWord(@NotNull String word){
        return blockedWords.contains(word);
    }

    public Set<String> getBlockedWords(){
        return blockedWords;
    }
}
