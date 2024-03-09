package pl.dream.dchatmanager.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dchatmanager.Locale;
import pl.dream.dchatmanager.Utils;
import pl.dream.dchatmanager.controller.ChatController;
import pl.dream.dreamlib.Message;

import java.util.UUID;

public class ChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("cc")){
            if(checkPermission(sender, "dchatmanager.chat.clear")){
                Utils.clearChat(sender);
            }

            return true;
        }

        if(args.length==0){
            if(checkPermission(sender, "dchatmanager.chat.help")){
                Message.sendMessage(sender, Locale.CHAT_HELP.getList());
            }

            return true;
        }
        else if(args.length==1){
            if(args[0].equalsIgnoreCase("on")){
                if(checkPermission(sender, "dchatmanager.chat.on")){
                    Utils.turnOnChat(sender);
                }
            }
            else if(args[0].equalsIgnoreCase("off")){
                if(checkPermission(sender, "dchatmanager.chat.off")){
                    Utils.turnOffChat(sender);
                }
            }
            else if(args[0].equalsIgnoreCase("clear")){
                if(checkPermission(sender, "dchatmanager.chat.clear")){
                    Utils.clearChat(sender);
                }
            }
            else if(args[0].equalsIgnoreCase("reload")){
                if(checkPermission(sender, "dchatmanager.chat.reload")){
                    DChatManager.getPlugin().reloadPlugin();
                }
            }

            return true;
        }
        else if(args.length==2){
            if(checkPermission(sender, "dchatmanager.blockedmessages.display")){
                //Show blocked message after click
                if(args[0].equalsIgnoreCase("sbm")){
                    String playerUUID = args[1];
                    String message = ChatController.getInstance().getBlockedMessages(UUID.fromString(playerUUID));

                    if(message!=null){
                        for(Player player: Bukkit.getOnlinePlayers()){
                            if(player.getUniqueId().toString().equalsIgnoreCase(playerUUID)){
                                continue;
                            }

                            player.sendMessage(message);
                        }
                    }
                }
            }

            return true;
        }
        else {
            if(args[0].equalsIgnoreCase("blockedWords")){
                if(checkPermission(sender, "dchatmanager.chat.blockedwords.manage")){
                    StringBuilder blockedWord = new StringBuilder();

                    for(int i=2;i<args.length;i++){
                        blockedWord.append(args[i]);

                        if(i+1<args.length){
                            blockedWord.append(" ");
                        }
                    }

                    if(args[1].equalsIgnoreCase("add")){
                        if(ChatController.getInstance().antiSwearing.addBlockedWord(blockedWord.toString())){
                            Message.sendMessage(sender, Locale.CHAT_BLOCKED_WORDS_ADD_CORRECT.getText());
                        }
                        else{
                            Message.sendMessage(sender, Locale.CHAT_BLOCKED_WORDS_ADD_INCORRECT.getText());
                        }
                    }
                    else if(args[1].equalsIgnoreCase("remove")){
                        if(ChatController.getInstance().antiSwearing.removeBlockedWord(blockedWord.toString())){
                            Message.sendMessage(sender, Locale.CHAT_BLOCKED_WORDS_REMOVE_CORRECT.getText());
                        }
                        else{
                            Message.sendMessage(sender, Locale.CHAT_BLOCKED_WORDS_REMOVE_INCORRECT.getText());
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean checkPermission(CommandSender sender, String permission){
        if(sender.hasPermission("dchatmanager.chat.clear")){
            return true;
        }
        Message.sendMessage(sender, Locale.NO_PERMISSION.getText());

        return false;
    }
}
