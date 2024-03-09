package pl.dream.dchatmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.dream.dchatmanager.controller.ConfigController;

public final class DChatManager extends JavaPlugin {
    private static DChatManager plugin;

    public ConfigController configController;

    @Override
    public void onEnable() {
        plugin = this;

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
