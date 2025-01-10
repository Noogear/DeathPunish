package cn.deathPunish;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Listeners implements Listener {
    private final Main plugin;
    private final Config config;
    private final ConcurrentHashMap<UUID, Integer> foodLevel;

    public Listeners(Main main) {
        this.plugin = main;
        this.config = plugin.getconfig();
        foodLevel = new ConcurrentHashMap<>();
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        @NotNull Player player = event.getPlayer();

        foodLevel.put(player.getUniqueId(), player.getFoodLevel());

    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        @NotNull Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (config.isKeepFoodLevel()) {
                if (foodLevel.containsKey(player.getUniqueId())) {
                    player.setFoodLevel(foodLevel.get(player.getUniqueId()));
                }
            }
            int potionTime = config.getPotionTime();
            for (PotionEffectType potion : config.getPotionTypes()) {
                player.addPotionEffect(new PotionEffect(potion, potionTime, config.getPotionLevel()));
            }
        }, 1);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            String title = config.getTitle().get(ThreadLocalRandom.current().nextInt(config.getTitle().size()));
            String subtitle = config.getSubtitle().get(ThreadLocalRandom.current().nextInt(config.getSubtitle().size()));
            player.sendTitle(title, subtitle, 20, 70, 20);
        }, 1);

    }

}
