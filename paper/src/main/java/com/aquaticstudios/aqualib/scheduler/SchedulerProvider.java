package com.aquaticstudios.aqualib.scheduler;

import com.aquaticstudios.aqualib.platform.PlatformProvider;
import com.aquaticstudios.aqualib.scheduler.impl.BukkitScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SchedulerProvider {

    private static final String FOLIA_SCHEDULER = "com.aquaticstudios.aqualib.scheduler.impl.FoliaScheduler";

    private static Scheduler scheduler;

    @NotNull
    public static Scheduler init(@NotNull JavaPlugin plugin) {
        scheduler = PlatformProvider.platform().isFolia()
                ? loadFolia(plugin)
                : new BukkitScheduler(plugin);
        return scheduler;
    }

    @NotNull
    public static Scheduler get() {
        if (scheduler == null) {
            throw new IllegalStateException("Scheduler not initialized; call AquaLib.bootstrap(plugin) first.");
        }
        return scheduler;
    }

    @NotNull
    private static Scheduler loadFolia(@NotNull JavaPlugin plugin) {
        try {
            Class<?> clazz = Class.forName(FOLIA_SCHEDULER);
            return (Scheduler) clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
        } catch (ReflectiveOperationException exception) {

            return new BukkitScheduler(plugin);
        }
    }
}
