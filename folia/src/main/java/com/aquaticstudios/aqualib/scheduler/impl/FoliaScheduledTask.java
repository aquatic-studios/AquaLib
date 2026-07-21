package com.aquaticstudios.aqualib.scheduler.impl;

import com.aquaticstudios.aqualib.scheduler.ScheduledTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask.ExecutionState;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class FoliaScheduledTask implements ScheduledTask {

    private final io.papermc.paper.threadedregions.scheduler.ScheduledTask task;

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static FoliaScheduledTask nullable(@Nullable io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
        return task == null ? null : new FoliaScheduledTask(task);
    }

    @Override
    @NotNull
    public Plugin getOwningPlugin() {
        return task.getOwningPlugin();
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        ExecutionState state = task.getExecutionState();
        return state == ExecutionState.CANCELLED || state == ExecutionState.CANCELLED_RUNNING;
    }
}
