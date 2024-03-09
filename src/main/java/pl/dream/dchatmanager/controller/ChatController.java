package pl.dream.dchatmanager.controller;

import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.data.AntiSpam;
import pl.dream.dchatmanager.data.AntiSwearing;
import pl.dream.dchatmanager.data.ChatFeature;

public class ChatController {
    private static ChatController instance;

    //Features
    public ChatFeature chat;
    public AntiSpam antiSpam;
    public ChatFeature antiFlood;
    public ChatFeature antiCaps;
    public AntiSwearing antiSwearing;

    public static ChatController getInstance(){
        if(instance==null){
            instance = new ChatController();
        }

        return instance;
    }
}
