package com.aquaticstudios.aqualib.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface Scheduler {

    void run(@NotNull Consumer<ScheduledTask> task);

    @NotNull
    ScheduledTask run(@NotNull Runnable task);

    void execute(@NotNull Runnable runnable);

    void runAsync(@NotNull Runnable runnable);

    void runLater(@NotNull Consumer<ScheduledTask> task, long ticksLater);

    @NotNull
    ScheduledTask runLater(@NotNull Runnable task, long ticksLater);

    void runLaterAsync(@NotNull Consumer<ScheduledTask> task, long ticksLater);

    @NotNull
    ScheduledTask runLaterAsync(@NotNull Runnable task, long ticksLater);

    void runTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period);

    @NotNull
    ScheduledTask runTimer(@NotNull Runnable task, long delay, long period);

    void runAsyncTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period);

    @NotNull
    ScheduledTask runAsyncTimer(@NotNull Runnable task, long delay, long period);

    void runAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task);

    @NotNull
    ScheduledTask runAt(@NotNull Location location, @NotNull Runnable task);

    void runTimerAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long delay, long period);

    @NotNull
    ScheduledTask runTimerAt(@NotNull Location location, @NotNull Runnable task, long delay, long period);

    void runLaterAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long ticksLater);

    @NotNull
    ScheduledTask runLaterAt(@NotNull Location location, @NotNull Runnable task, long ticksLater);

    void executeAt(@NotNull Location location, @NotNull Runnable runnable);

    void run(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired);

    void execute(@NotNull Entity entity, @NotNull Runnable run, @NotNull Runnable retired, long delay);

    void runTaskTimer(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks);

    @NotNull
    ScheduledTask runTaskTimer(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks);

    void runLater(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long delayTicks);

    @NotNull
    ScheduledTask runLater(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long delayTicks);

    boolean isOwnedByCurrentRegion(@NotNull Location location);

    boolean isGlobalTickThread();

    void cancelAll();
}
