package cn.deathPunish;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Config {
    private final Main plugin;
    private boolean enabled;
    private List<PotionEffectType> potionTypes;
    private int potionTime;
    private int potionLevel;
    private List<String> title;
    private List<String> subtitle;
    private boolean keepFoodLevel;

    public Config(Main main) {
        this.plugin = main;
        loadConfig();

    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        enabled = plugin.getConfig().getBoolean("enabled", true);
        potionTypes = potionToList(plugin.getConfig().getStringList("potion-effect.type"));
        potionTime = plugin.getConfig().getInt("potion-effect.time", 30);
        potionLevel = plugin.getConfig().getInt("potion-effect.level", 1);
        title = colorList(plugin.getConfig().getStringList("title"));
        subtitle = colorList(plugin.getConfig().getStringList("subtitle"));
        keepFoodLevel = plugin.getConfig().getBoolean("keep-food-level", true);

    }

    private List<PotionEffectType> potionToList(List<String> potions) {
        return potions.stream()
                .map(String::toLowerCase)
                .map(s -> {
                    try {
                        return Registry.POTION_EFFECT_TYPE.get(Objects.requireNonNull(NamespacedKey.fromString(s)));
                    } catch (NullPointerException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> colorList(List<String> text) {
        return text.stream()
                .filter(Objects::nonNull)
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .collect(Collectors.toList());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<PotionEffectType> getPotionTypes() {
        return potionTypes;
    }

    public int getPotionTime() {
        return potionTime;
    }

    public int getPotionLevel() {
        return potionLevel;
    }

    public List<String> getTitle() {
        return title;
    }

    public List<String> getSubtitle() {
        return subtitle;
    }

    public boolean isKeepFoodLevel() {
        return keepFoodLevel;
    }
}
