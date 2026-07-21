package com.aquaticstudios.aqualib;

import com.aquaticstudios.aqualib.command.CommandManager;
import com.aquaticstudios.aqualib.config.Config;
import com.aquaticstudios.aqualib.config.ConfigFactory;
import com.aquaticstudios.aqualib.config.exception.ConfigLoadException;
import com.aquaticstudios.aqualib.scheduler.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public abstract class AquaPlugin extends JavaPlugin {

    @Override
    public final void onLoad() {
        load();
    }

    @Override
    public final void onEnable() {
        AquaLib.bootstrap(this);
        enable();
    }

    @Override
    public final void onDisable() {
        disable();
        AquaLib.shutdown();
    }

    protected void load() {
    }

    protected abstract void enable();

    protected void disable() {
    }

    @NotNull
    protected final Scheduler scheduler() {
        return AquaLib.scheduler();
    }

    @NotNull
    protected final CommandManager commands() {
        return AquaLib.commands();
    }

    @NotNull
    protected final Config versionedConfig(@NotNull String fileName) {
        return load(fileName, true);
    }

    @NotNull
    protected final Config plainConfig(@NotNull String fileName) {
        return load(fileName, false);
    }

    @NotNull
    private Config load(@NotNull String fileName, boolean versioned) {
        File file = new File(getDataFolder(), fileName);
        InputStream resource = getResource(fileName);
        try {
            return versioned
                    ? ConfigFactory.versioned(file, resource)
                    : ConfigFactory.plain(file, resource);
        } catch (ConfigLoadException exception) {
            getLogger().severe(exception.getMessage());
            throw new ConfigLoadException("Invalid " + fileName + " (see the error above).");
        }
    }
}
