package pl.dream.dchatmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.command.ChatCommand;
import pl.dream.dchatmanager.command.ChatTabCompleter;
import pl.dream.dchatmanager.controller.AutoMessageController;
import pl.dream.dchatmanager.controller.ConfigController;
import pl.dream.dchatmanager.listener.PlayerQuitListener;
import pl.dream.dchatmanager.listener.SendMessageListener;

import java.util.HashMap;
import java.util.UUID;

public final class DChatManager extends JavaPlugin {
    private static DChatManager plugin;

    public ConfigController configController;
    public AutoMessageController autoMessageController;

    public HashMap<UUID, Long> playerCooldownList;


    @Override
    public void onEnable() {
        plugin = this;
        playerCooldownList = new HashMap<>();

        getCommand("chat").setExecutor(new ChatCommand());
        getCommand("chat").setTabCompleter(new ChatTabCompleter());

        getServer().getPluginManager().registerEvents(new SendMessageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        autoMessageController = new AutoMessageController();

        loadPlugin();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin(){
        reloadConfig();

        loadPlugin();
    }

    private void loadPlugin(){
        saveDefaultConfig();
        Locale.loadMessages(this);

        configController = new ConfigController(getConfig());
    }


    public static @NotNull DChatManager getPlugin(){
        return plugin;
    }
}
