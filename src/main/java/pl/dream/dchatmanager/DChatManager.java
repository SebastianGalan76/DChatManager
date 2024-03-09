package pl.dream.dchatmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.controller.ConfigController;
import pl.dream.dchatmanager.listener.SendMessageListener;

import java.util.HashMap;
import java.util.UUID;

public final class DChatManager extends JavaPlugin {
    private static DChatManager plugin;

    public ConfigController configController;
    public HashMap<UUID, Long> playerCooldownList;


    @Override
    public void onEnable() {
        plugin = this;
        playerCooldownList = new HashMap<>();

        getServer().getPluginManager().registerEvents(new SendMessageListener(), this);

        loadPlugin();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadPlugin(){
        configController = new ConfigController(getConfig());
    }

    public static @NotNull DChatManager getPlugin(){
        return plugin;
    }
}
