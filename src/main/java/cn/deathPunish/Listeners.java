package cn.deathPunish;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Listeners implements Listener {
    private final Main plugin;
    private final Config config;
    private final HashMap<UUID, Integer> foodLevel;

    public Listeners(Main main) {
        this.plugin = main;
        this.config = plugin.getconfig();
        foodLevel = new HashMap<>();
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (config.isKeepFoodLevel()) {
            @NotNull Player player = event.getPlayer();
            foodLevel.put(player.getUniqueId(), player.getFoodLevel());
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        @NotNull Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
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

        if (config.isKeepFoodLevel()) {
            Integer food = foodLevel.getOrDefault(player.getUniqueId(), 20);
            if (food != 20) {
                player.setFoodLevel(food);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        foodLevel.remove(event.getPlayer().getUniqueId());
    }

}
