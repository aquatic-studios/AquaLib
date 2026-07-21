package com.aquaticstudios.aqualib.scheduler.impl;

import com.aquaticstudios.aqualib.scheduler.ScheduledTask;
import com.aquaticstudios.aqualib.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@RequiredArgsConstructor
public final class BukkitScheduler implements Scheduler {

    private final JavaPlugin plugin;

    @Override
    public void run(@NotNull Consumer<ScheduledTask> task) {
        ScheduledTask[] holder = new ScheduledTask[1];
        BukkitTask handle = Bukkit.getScheduler().runTask(plugin, () -> task.accept(holder[0]));
        holder[0] = new BukkitScheduledTask(handle);
    }

    @Override
    @NotNull
    public ScheduledTask run(@NotNull Runnable task) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTask(plugin, task));
    }

    @Override
    public void execute(@NotNull Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runAsync(@NotNull Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void runLater(@NotNull Consumer<ScheduledTask> task, long ticksLater) {
        ScheduledTask[] holder = new ScheduledTask[1];
        BukkitTask handle = Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(holder[0]), ticksLater);
        holder[0] = new BukkitScheduledTask(handle);
    }

    @Override
    @NotNull
    public ScheduledTask runLater(@NotNull Runnable task, long ticksLater) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLater(plugin, task, ticksLater));
    }

    @Override
    public void runLaterAsync(@NotNull Consumer<ScheduledTask> task, long ticksLater) {
        ScheduledTask[] holder = new ScheduledTask[1];
        BukkitTask handle = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> task.accept(holder[0]), ticksLater);
        holder[0] = new BukkitScheduledTask(handle);
    }

    @Override
    @NotNull
    public ScheduledTask runLaterAsync(@NotNull Runnable task, long ticksLater) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, ticksLater));
    }

    @Override
    public void runTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period) {
        ScheduledTask[] holder = new ScheduledTask[1];
        BukkitTask handle = Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(holder[0]), delay, period);
        holder[0] = new BukkitScheduledTask(handle);
    }

    @Override
    @NotNull
    public ScheduledTask runTimer(@NotNull Runnable task, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period));
    }

    @Override
    public void runAsyncTimer(@NotNull Consumer<ScheduledTask> task, long delay, long period) {
        ScheduledTask[] holder = new ScheduledTask[1];
        BukkitTask handle = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> task.accept(holder[0]), delay, period);
        holder[0] = new BukkitScheduledTask(handle);
    }

    @Override
    @NotNull
    public ScheduledTask runAsyncTimer(@NotNull Runnable task, long delay, long period) {
        return new BukkitScheduledTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period));
    }

    @Override
    public void runAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task) {
        run(task);
    }

    @Override
    @NotNull
    public ScheduledTask runAt(@NotNull Location location, @NotNull Runnable task) {
        return run(task);
    }

    @Override
    public void runTimerAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long delay, long period) {
        runTimer(task, delay, period);
    }

    @Override
    @NotNull
    public ScheduledTask runTimerAt(@NotNull Location location, @NotNull Runnable task, long delay, long period) {
        return runTimer(task, delay, period);
    }

    @Override
    public void runLaterAt(@NotNull Location location, @NotNull Consumer<ScheduledTask> task, long ticksLater) {
        runLater(task, ticksLater);
    }

    @Override
    @NotNull
    public ScheduledTask runLaterAt(@NotNull Location location, @NotNull Runnable task, long ticksLater) {
        return runLater(task, ticksLater);
    }

    @Override
    public void executeAt(@NotNull Location location, @NotNull Runnable runnable) {
        execute(runnable);
    }

    @Override
    public void run(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired) {
        run(task);
    }

    @Override
    public void execute(@NotNull Entity entity, @NotNull Runnable run, @NotNull Runnable retired, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, run, Math.max(1L, delay));
    }

    @Override
    public void runTaskTimer(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks) {
        runTimer(task, initialDelayTicks, periodTicks);
    }

    @Override
    @NotNull
    public ScheduledTask runTaskTimer(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long initialDelayTicks, long periodTicks) {
        return runTimer(task, initialDelayTicks, periodTicks);
    }

    @Override
    public void runLater(@NotNull Entity entity, @NotNull Consumer<ScheduledTask> task, @NotNull Runnable retired, long delayTicks) {
        runLater(task, delayTicks);
    }

    @Override
    @NotNull
    public ScheduledTask runLater(@NotNull Entity entity, @NotNull Runnable task, @NotNull Runnable retired, long delayTicks) {
        return runLater(task, delayTicks);
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public boolean isGlobalTickThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
