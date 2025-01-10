package cn.deathPunish;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private Config config;
    private boolean isListening;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new Config(this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        isListening = true;
        getCommand("notarget").setExecutor(new Commands(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Config getconfig() {
        return config;
    }

    public void reload() {
        config.loadConfig();
        if (config.isEnabled()) {
            if (!isListening) {
                Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
                isListening = true;
            }
        } else {
            if (isListening) {
                HandlerList.unregisterAll();
                isListening = false;
            }
        }
    }

}
