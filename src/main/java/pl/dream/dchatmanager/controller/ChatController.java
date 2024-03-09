package pl.dream.dchatmanager.controller;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pl.dream.dchatmanager.data.AntiCaps;
import pl.dream.dchatmanager.data.AntiSpam;
import pl.dream.dchatmanager.data.AntiSwearing;
import pl.dream.dchatmanager.data.ChatFeature;

import java.util.HashMap;
import java.util.UUID;

public class ChatController {
    private static ChatController instance;

    //Features
    public ChatFeature chat;
    public AntiSpam antiSpam;
    public ChatFeature antiFlood;
    public AntiCaps antiCaps;
    public AntiSwearing antiSwearing;

    private HashMap<UUID, String> blockedMessages;

    public static ChatController getInstance(){
        if(instance==null){
            instance = new ChatController();
        }

        return instance;
    }

    public void addBlockedMessages(Player sender, String formattedMessage){
        if(blockedMessages==null){
            blockedMessages = new HashMap<>();
        }

        blockedMessages.put(sender.getUniqueId(), formattedMessage);
    }

    public @Nullable String getBlockedMessages(UUID senderUUID){
        if(blockedMessages==null){
            return null;
        }

        return blockedMessages.remove(senderUUID);
    }
}
