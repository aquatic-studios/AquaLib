package com.aquaticstudios.aqualib.scheduler.impl;

import com.aquaticstudios.aqualib.scheduler.ScheduledTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class BukkitScheduledTask implements ScheduledTask {

    private final BukkitTask task;

    @Override
    @NotNull
    public Plugin getOwningPlugin() {
        return task.getOwner();
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
}
