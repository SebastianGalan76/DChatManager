package pl.dream.dchatmanager.controller;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.DChatManager;
import pl.dream.dchatmanager.data.AntiSpam;
import pl.dream.dchatmanager.data.AntiSwearing;
import pl.dream.dchatmanager.data.ChatFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigController {
    private final FileConfiguration config;

    public ConfigController(FileConfiguration config){
        this.config = config;

        loadConfig();
    }

    private void loadConfig(){
        DChatManager.getPlugin().saveDefaultConfig();

        loadFeatures();
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

        controller.antiCaps = new ChatFeature(enabled);
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
}
