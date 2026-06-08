package net.milkbowl.vault.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class FoliaScheduler {
    private static final boolean FOLIA;

    static {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException ignored) {
        }
        FOLIA = folia;
    }

    private FoliaScheduler() {
    }

    public static void cancelTasks(Plugin plugin) {
        if (FOLIA) {
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
            Bukkit.getAsyncScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    public static void runGlobal(Plugin plugin, Runnable runnable) {
        if (FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runAsyncRepeating(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (FOLIA) {
            long delayMs = Math.max(0L, delayTicks) * 50L;
            long periodMs = Math.max(1L, periodTicks) * 50L;
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> runnable.run(), delayMs, periodMs, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delayTicks, periodTicks);
        }
    }
}
