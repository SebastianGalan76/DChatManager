package pl.dream.dchatmanager.data;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dreamlib.Message;

import java.util.List;

public class AutoMessage {
    private final List<String> messages;
    private final List<String> commands;
    private final Sound sound;

    public AutoMessage(List<String> messages, List<String> commands, Sound sound) {
        this.messages = messages;
        this.commands = commands;
        this.sound = sound;
    }

    public void send() {
        if (messages != null && !messages.isEmpty()) {
            Message.sendGlobalMessage(messages);
        }
        if (commands != null && !commands.isEmpty()) {
            ConsoleCommandSender sender = Bukkit.getConsoleSender();

            Bukkit.getScheduler().runTask(DChatManager.getPlugin(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String line : commands) {
                        Bukkit.dispatchCommand(sender, line.replace("{PLAYER}", player.getName()));
                    }
                }
            });
        }
        if (sound != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound.sound, sound.volume, sound.pitch);
            }
        }
    }

    public static class Sound {
        public org.bukkit.Sound sound;
        public float volume;
        public float pitch;
    }
}
