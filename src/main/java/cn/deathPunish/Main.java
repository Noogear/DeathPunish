package cn.deathPunish;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    private Config config;
    private boolean isListening = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = new Config(this);
        if (config.isEnabled()) {
            Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
            isListening = true;
        }
        Objects.requireNonNull(getCommand("deathpunish")).setExecutor(new Commands(this));

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
