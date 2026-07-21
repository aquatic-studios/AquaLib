package com.aquaticstudios.aqualib.scheduler;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ScheduledTask {

    @NotNull
    Plugin getOwningPlugin();

    void cancel();

    boolean isCancelled();
}
