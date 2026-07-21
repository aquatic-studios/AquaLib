package com.aquaticstudios.aqualib;

import com.aquaticstudios.aqualib.command.CommandManager;
import com.aquaticstudios.aqualib.platform.Platform;
import com.aquaticstudios.aqualib.platform.PlatformProvider;
import com.aquaticstudios.aqualib.scheduler.Scheduler;
import com.aquaticstudios.aqualib.scheduler.SchedulerProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AquaLib {

    private static JavaPlugin plugin;
    private static CommandManager commandManager;

    public static void bootstrap(@NotNull JavaPlugin owner) {
        plugin = owner;
        SchedulerProvider.init(owner);
        commandManager = new CommandManager(owner);
    }

    public static void shutdown() {
        if (plugin != null) {
            SchedulerProvider.get().cancelAll();
        }
        plugin = null;
        commandManager = null;
    }

    @NotNull
    public static JavaPlugin plugin() {
        ensureReady();
        return plugin;
    }

    @NotNull
    public static Scheduler scheduler() {
        return SchedulerProvider.get();
    }

    @NotNull
    public static CommandManager commands() {
        ensureReady();
        return commandManager;
    }

    @NotNull
    public static Platform platform() {
        return PlatformProvider.platform();
    }

    private static void ensureReady() {
        if (plugin == null) {
            throw new IllegalStateException("AquaLib is not bootstrapped; call AquaLib.bootstrap(plugin) first.");
        }
    }
}
