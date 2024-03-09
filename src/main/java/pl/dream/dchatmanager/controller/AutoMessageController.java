package pl.dream.dchatmanager.controller;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dchatmanager.data.AutoMessage;

public class AutoMessageController {
    private AutoMessage[] messages;

    private int currentMessageIndex;
    private BukkitTask autoMessageTask;

    public AutoMessageController(){
        currentMessageIndex = 0;
    }

    public void loadController(AutoMessage[] messages, long interval){
        this.messages = messages;

        if(autoMessageTask!=null){
            autoMessageTask.cancel();
        }

        if(messages.length==0){
            return;
        }

        autoMessageTask = Bukkit.getScheduler().runTaskTimerAsynchronously(DChatManager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                messages[getMessageIndex()].send();
            }
        }, 0, interval);
    }

    private int getMessageIndex(){
        currentMessageIndex++;

        if(currentMessageIndex>=messages.length){
            currentMessageIndex = 0;
        }

        return currentMessageIndex;
    }
}
