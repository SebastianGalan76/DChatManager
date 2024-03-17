package pl.dream.dchatmanager.listener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dchatmanager.Locale;
import pl.dream.dchatmanager.controller.ChatController;
import pl.dream.dchatmanager.controller.Tokenizer;
import pl.dream.dreamlib.Color;
import pl.dream.dreamlib.Message;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class SendMessageListener implements Listener {
    private final Tokenizer tokenizer;
    public HashMap<UUID, Long> playerCooldownList;

    public SendMessageListener(){
        playerCooldownList = new HashMap<>();
        tokenizer = new Tokenizer();
    }

    @EventHandler
    public void onPlayerSendMessage(AsyncPlayerChatEvent e){
        if(e.isCancelled()){
            return;
        }

        Player player = e.getPlayer();
        ChatController controller = ChatController.getInstance();

        //Check if the chat is turned off
        if(!controller.chat.isEnabled()){
            if(!player.hasPermission("dchatmanager.chat.off.bypass")){
                e.setCancelled(true);
                Message.sendMessage(player, Locale.CHAT_OFF_NO_PERMISSION.toString());

                return;
            }
        }

        //Check sending cooldown
        if(controller.antiSpam.isEnabled()){
            if(!player.hasPermission("dchatmanager.cooldown.bypass")){
                long lastTimeSendMessage = 0;
                if(playerCooldownList.containsKey(player.getUniqueId())){
                    lastTimeSendMessage = playerCooldownList.get(player.getUniqueId());
                }

                long cooldown = Calendar.getInstance().getTimeInMillis() - lastTimeSendMessage;
                if(cooldown < controller.antiSpam.getCooldown()){
                    e.setCancelled(true);
                    Message.sendMessage(player, Locale.COOLDOWN_MESSAGE.toString());
                    return;
                }
            }

            playerCooldownList.put(player.getUniqueId(), Calendar.getInstance().getTimeInMillis() + controller.antiSpam.getCooldown());
        }

        String message = e.getMessage();

        //Check if the player has sent blocked words
        if(controller.antiSwearing.isEnabled()){
            if(!player.hasPermission("dchatmanager.antiswearing.bypass")){
                String tokenizedMessage = tokenizer.getTokenizedText(message);

                for(String blockedWord:controller.antiSwearing.getBlockedWords()){
                    if(tokenizedMessage.contains(blockedWord)){
                        //Remove all recipients and only send the message to the sending player.
                        e.getRecipients().clear();
                        e.getRecipients().add(player);

                        String formattedMessage = String.format(e.getFormat(), player.getDisplayName(), e.getMessage());
                        controller.addBlockedMessages(player, formattedMessage);

                        sendNotificationToAdmin(player, formattedMessage, blockedWord);

                        return;
                    }
                }
            }
        }

        //Remove flood from sent message
        if(controller.antiFlood.isEnabled()){
            if(!player.hasPermission("dchatmanager.antiflood.bypass")){
                message = tokenizer.removeFlood(message);
                e.setMessage(message);
            }
        }

        //Check if player send message with caps lock
        if(controller.antiCaps.isEnabled()){
            if(!player.hasPermission("dchatmanager.anticaps.bypass")){
                int messageLength = message.length();

                if(messageLength >= controller.antiCaps.getMinMessageLength()){
                    StringBuilder result = new StringBuilder();
                    boolean isFirstLetter = true;
                    int uppercaseCount = 0;
                    for (int i = 0; i < messageLength; i++) {
                        char c = message.charAt(i);

                        if(Character.isUpperCase(c)){
                            if(isFirstLetter){
                                result.append(c);
                                isFirstLetter = false;
                            }
                            else{
                                result.append(Character.toLowerCase(c));
                            }

                            uppercaseCount++;
                        }
                        else{
                            result.append(c);
                        }
                    }

                    float percentage = (float) uppercaseCount / messageLength * 100f;
                    if(percentage>=controller.antiCaps.getMaxCapsPercent()){
                        //Convert sent message to lower case
                        message = result.toString();
                        e.setMessage(message);
                    }
                }
            }
        }
    }

    private void sendNotificationToAdmin(Player sender, String formattedMessage, String blockedWord){
        TextComponent messageComponent = new TextComponent(Color.fix("&c&l[!] " + formattedMessage));
        messageComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat sbm "+sender.getUniqueId().toString()));
        messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Locale.CLICK_TO_SHOW_BLOCKED_MESSAGE.toString()
                .replace("{BLOCKED_WORD}", blockedWord))));

        for(Player player: Bukkit.getOnlinePlayers()){
            if(player.hasPermission("dchatmanager.antiswearing.notification")){
                player.sendMessage(messageComponent);
            }
        }
    }
}
