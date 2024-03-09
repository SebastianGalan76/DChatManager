package pl.dream.dchatmanager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.dream.dchatmanager.controller.ChatController;
import pl.dream.dreamlib.Color;
import pl.dream.dreamlib.Message;

public class Utils {
    public static void clearChat(CommandSender sender){
        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.hasPermission("dchatmanager.clear.bypass")){
                Message.sendMessage(sender, "");
            }
        }

        for(String line:Locale.CHAT_CLEAR_NOTIFICATION.getList()){
            if(sender instanceof Player){
                line = line.replace("{PLAYER}", ((Player)sender).getName());
            }
            else{
                line = line.replace("{PLAYER}", "Konsole");
            }

            Color.sendBroadcast(line);
        }
    }

    public static void turnOffChat(CommandSender sender){
        if(!ChatController.getInstance().chat.isEnabled()){
            Message.sendMessage(sender, Locale.CHAT_OFF_ALREADY_OFF.getText());
            return;
        }

        ChatController.getInstance().chat.enable(false);

        for(String line:Locale.CHAT_OFF_NOTIFICATION.getList()){
            if(sender instanceof Player){
                line = line.replace("{PLAYER}", ((Player)sender).getName());
            }
            else{
                line = line.replace("{PLAYER}", "Konsole");
            }

            Color.sendBroadcast(line);
        }
    }

    public static void turnOnChat(CommandSender sender){
        if(ChatController.getInstance().chat.isEnabled()){
            Message.sendMessage(sender, Locale.CHAT_ON_ALREADY_ON.getText());
            return;
        }

        ChatController.getInstance().chat.enable(true);

        for(String line:Locale.CHAT_ON_NOTIFICATION.getList()){
            if(sender instanceof Player){
                line = line.replace("{PLAYER}", ((Player)sender).getName());
            }
            else{
                line = line.replace("{PLAYER}", "Konsole");
            }

            Color.sendBroadcast(line);
        }
    }
}
