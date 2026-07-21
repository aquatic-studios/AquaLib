package com.aquaticstudios.aqualib.scheduler.impl;

import com.aquaticstudios.aqualib.scheduler.ScheduledTask;
import com.aquaticstudios.aqualib.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class FoliaScheduler implements Scheduler {

    private final JavaPlugin plugin;

    @Override
    public void run(@NotNull Consumer<ScheduledTask> task) {
        Bukkit.getGlobalRegionScheduler().run(plugin, handle -> task.accept(new FoliaScheduledTask(handle)));
    }

    @Override
    @NotNull
    public ScheduledTask run(@NotNull Runnable task) {
        return new FoliaScheduledTask(Bukkit.getGlobalRegionScheduler().run(plugin, handle -> task.run()));
    }

    @Override
    public void execute(@NotNull Runnable runnable) {
        Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        Bukkit.getAsyncScheduler().runNow(plugin, handle -> runnable.run());
    }

    @Override
    public void runLater(@NotNull Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), ticksLater);
    }

    @Override
    @NotNull
    public ScheduledTask runLater(@NotNull Runnable task, long ticksLater) {
        return new FoliaScheduledTask(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, handle -> task.run(), ticksLater));
    }

    @Override
    public void runLaterAsync(@NotNull Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getAsyncScheduler().runDelayed(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), ticksLater * 50L, TimeUnit.MILLISECONDS);
    }

    @Override
    @NotNull
    public ScheduledTask runLaterAsync(@NotNull Runnable task, long ticksLater) {
        return new FoliaScheduledTask(Bukkit.getAsyncScheduler().runDelayed(plugin, handle -> task.run(), ticksLater * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public void runTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), Math.max(1L, delay), period);
    }

    @Override
    @NotNull
    public ScheduledTask runTimer(@NotNull Runnable task, long delay, long period) {
        return new FoliaScheduledTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, handle -> task.run(), Math.max(1L, delay), period));
    }

    @Override
    public void runAsyncTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), delay * 50L, period * 50L, TimeUnit.MILLISECONDS);
    }

    @Override
    @NotNull
    public ScheduledTask runAsyncTimer(@NotNull Runnable task, long delay, long period) {
        return new FoliaScheduledTask(Bukkit.getAsyncScheduler().runAtFixedRate(plugin, handle -> task.run(), delay * 50L, period * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public void runAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task) {
        Bukkit.getRegionScheduler().run(plugin, location, handle -> task.accept(new FoliaScheduledTask(handle)));
    }

    @Override
    @NotNull
    public ScheduledTask runAt(@NotNull Location location, @NotNull Runnable task) {
        return new FoliaScheduledTask(Bukkit.getRegionScheduler().run(plugin, location, handle -> task.run()));
    }

    @Override
    public void runTimerAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long delay, long period) {
        Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, handle -> task.accept(new FoliaScheduledTask(handle)), Math.max(1L, delay), period);
    }

    @Override
    @NotNull
    public ScheduledTask runTimerAt(@NotNull Location location, @NotNull Runnable task, long delay, long period) {
        return new FoliaScheduledTask(Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, handle -> task.run(), Math.max(1L, delay), period));
    }

    @Override
    public void runLaterAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long ticksLater) {
        Bukkit.getRegionScheduler().runDelayed(plugin, location, handle -> task.accept(new FoliaScheduledTask(handle)), ticksLater);
    }

    @Override
    @NotNull
    public ScheduledTask runLaterAt(@NotNull Location location, @NotNull Runnable task, long ticksLater) {
        return new FoliaScheduledTask(Bukkit.getRegionScheduler().runDelayed(plugin, location, handle -> task.run(), ticksLater));
    }

    @Override
    public void executeAt(@NotNull Location location, @NotNull Runnable runnable) {
        Bukkit.getRegionScheduler().execute(plugin, location, runnable);
    }

    @Override
    public void run(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired) {
        entity.getScheduler().run(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), retired);
    }

    @Override
    public void execute(@NotNull Entity entity, @NotNull Runnable run, @NotNull Runnable retired, long delay) {
        entity.getScheduler().execute(plugin, run, retired, delay);
    }

    @Override
    public void runTaskTimer(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks) {
        entity.getScheduler().runAtFixedRate(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), retired, Math.max(1L, initialDelayTicks), periodTicks);
    }

    @Override
    @NotNull
    public ScheduledTask runTaskTimer(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks) {
        return new FoliaScheduledTask(entity.getScheduler().runAtFixedRate(plugin, handle -> task.run(), retired, Math.max(1L, initialDelayTicks), periodTicks));
    }

    @Override
    public void runLater(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long delayTicks) {
        entity.getScheduler().runDelayed(plugin, handle -> task.accept(new FoliaScheduledTask(handle)), retired, Math.max(1L, delayTicks));
    }

    @Override
    @NotNull
    public ScheduledTask runLater(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long delayTicks) {
        return new FoliaScheduledTask(entity.getScheduler().runDelayed(plugin, handle -> task.run(), retired, Math.max(1L, delayTicks)));
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return Bukkit.isOwnedByCurrentRegion(location);
    }

    @Override
    public boolean isGlobalTickThread() {
        return Bukkit.isGlobalTickThread();
    }

    @Override
    public void cancelAll() {
        Bukkit.getAsyncScheduler().cancelTasks(plugin);
        Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
    }
}
