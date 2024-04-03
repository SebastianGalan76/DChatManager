package pl.dream.dchatmanager.controller;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dchatmanager.data.AutoMessage;
import pl.dream.dchatmanager.data.feature.AntiCaps;
import pl.dream.dchatmanager.data.feature.AntiSpam;
import pl.dream.dchatmanager.data.feature.AntiSwearing;
import pl.dream.dchatmanager.data.feature.ChatFeature;
import pl.dream.dreamlib.Color;
import pl.dream.dreamlib.gradient.Gradient;
import pl.dream.dreamlib.gradient.LinearInterpolator;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigController {
    private final DChatManager plugin;
    private FileConfiguration config;

    public ConfigController(DChatManager plugin){
        this.plugin = plugin;

        loadConfig();
    }

    private void loadConfig(){
        DChatManager.getPlugin().saveDefaultConfig();
        config = plugin.getConfig();

        loadFeatures();
        loadChatFormats();
        loadAutoMessages();
    }

    public void reloadBlockedWord(){
        Set<String> blockedWords = ChatController.getInstance().antiSwearing.getBlockedWords();
        JavaPlugin plugin = DChatManager.getPlugin();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                File lang = new File(plugin.getDataFolder(), "blockedWords.yml");
                if (!lang.exists()) {
                    plugin.saveResource("blockedWords.yml", false);
                }

                FileConfiguration conf = YamlConfiguration.loadConfiguration(lang);
                conf.set("blockedWords", new ArrayList<>(blockedWords));

                try {
                    conf.save(lang);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadFeatures(){
        ChatController controller = ChatController.getInstance();

        loadChatSettings(controller);
        loadAntiSpam(controller);
        loadAntiFlood(controller);
        loadAntiCaps(controller);
        loadAntiSwearing(controller);
    }

    private void loadChatSettings(ChatController controller){
        boolean enabled = config.getBoolean("chat.enabled", false);

        controller.chat = new ChatFeature(enabled);
    }
    private void loadAntiFlood(ChatController controller){
        boolean enabled = config.getBoolean("antiFlood.enabled", false);

        controller.antiFlood = new ChatFeature(enabled);
    }
    private void loadAntiCaps(ChatController controller){
        boolean enabled = config.getBoolean("antiCaps.enabled", false);
        int minMessageLength = config.getInt("antiCaps.minMessageLength", 20);
        int maxCapsPercent = config.getInt("antiCaps.maxCapsPercent", 50);

        controller.antiCaps = new AntiCaps(enabled, minMessageLength, maxCapsPercent);
    }
    private void loadAntiSpam(ChatController controller){
        boolean enabled = config.getBoolean("antiSpam.enabled", false);
        int cooldown = config.getInt("antiSpam.cooldown", 3);

        controller.antiSpam = new AntiSpam(enabled, cooldown);
    }
    private void loadAntiSwearing(ChatController controller){
        boolean enabled = config.getBoolean("antiSwearing.enabled", false);
        Set<String> blockedWords = loadBlockedWords();

        controller.antiSwearing = new AntiSwearing(enabled, blockedWords);
    }

    private @NotNull Set<String> loadBlockedWords(){
        Set<String> blockedWords = new HashSet<>();
        JavaPlugin plugin = DChatManager.getPlugin();

        File lang = new File(plugin.getDataFolder(), "blockedWords.yml");
        if (!lang.exists()) {
            plugin.saveResource("blockedWords.yml", false);
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(lang);

        if(conf.getString("blockedWords")!=null){
            List<String> blockedWordsList = conf.getStringList("blockedWords");

            for(String string:blockedWordsList){
                blockedWords.add(string.toLowerCase());
            }
        }

        try {
            conf.save(lang);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return blockedWords;
    }

    private void loadChatFormats(){
        LinkedHashMap<String, String> chatFormats = new LinkedHashMap<>();

        for(String rank:config.getConfigurationSection("format").getKeys(false)){
            String format = config.getString("format."+rank);

            chatFormats.put(rank, format);
        }

        plugin.sendMessageListener.loadChatFormats(chatFormats);
    }

    private void loadAutoMessages(){
        if(config.get("autoMessage")==null || config.getConfigurationSection("autoMessage.messages")==null){
            return;
        }

        long interval = config.getLong("autoMessage.interval", 60);
        interval = interval * 20;

        List<AutoMessage> autoMessageList = new ArrayList<>();
        for(String path:config.getConfigurationSection("autoMessage.messages").getKeys(false)){
            if(!config.getBoolean("autoMessage.messages."+path+".enable", true)){
                continue;
            }

            List<String> messages = config.getStringList("autoMessage.messages."+path+".messages");
            List<String> commands = config.getStringList("autoMessage.messages."+path+".commands");
            AutoMessage.Sound sound;

            if(config.get("autoMessage.messages."+path+".sound")!=null){
                sound = new AutoMessage.Sound();

                try{
                    sound.sound = Sound.valueOf(config.getString("autoMessage.messages."+path+".sound.name"));
                }
                catch (IllegalArgumentException e){
                    sound = null;
                    e.printStackTrace();
                }

                if(sound!=null){
                    sound.volume = (float)config.getDouble("autoMessage.messages."+path+".sound.volume", 0.5);
                    sound.pitch = (float)config.getDouble("autoMessage.messages."+path+".sound.pitch", 0.5);
                }

                autoMessageList.add(new AutoMessage(messages, commands, sound));
            }
        }

        DChatManager.getPlugin().autoMessageController.loadController(autoMessageList.toArray(new AutoMessage[0]), interval);
    }
}
