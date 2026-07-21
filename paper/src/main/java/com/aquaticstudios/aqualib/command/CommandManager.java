package com.aquaticstudios.aqualib.command;

import com.aquaticstudios.aqualib.command.argument.ArgumentRegistry;
import com.aquaticstudios.aqualib.command.completion.CompletionRegistry;
import com.aquaticstudios.aqualib.command.internal.BukkitCommandBridge;
import com.aquaticstudios.aqualib.command.internal.RegisteredCommand;
import com.aquaticstudios.aqualib.utils.reflection.CommandSyncer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public final class CommandManager {

    private final JavaPlugin plugin;
    private final CommandMap commandMap;
    private final ArgumentRegistry arguments = new ArgumentRegistry();
    private final CompletionRegistry completions = new CompletionRegistry();

    public CommandManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.commandMap = resolveCommandMap();
    }

    @NotNull
    private static CommandMap resolveCommandMap() {
        Server server = Bukkit.getServer();
        try {
            Method method = server.getClass().getMethod("getCommandMap");
            method.setAccessible(true);
            return (CommandMap) method.invoke(server);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not access the server command map", exception);
        }
    }

    public void register(@NotNull BaseCommand command) {
        RegisteredCommand parsed = RegisteredCommand.of(command);
        BukkitCommandBridge bridge = new BukkitCommandBridge(parsed, arguments, completions);
        commandMap.register(plugin.getName().toLowerCase(), bridge);
        CommandSyncer.sync();
    }

    public void register(@NotNull BaseCommand... commands) {
        for (BaseCommand command : commands) {
            RegisteredCommand parsed = RegisteredCommand.of(command);
            BukkitCommandBridge bridge = new BukkitCommandBridge(parsed, arguments, completions);
            commandMap.register(plugin.getName().toLowerCase(), bridge);
        }
        CommandSyncer.sync();
    }

    public void register(@NotNull BaseCommand command, @NotNull List<String> namesAndAliases) {
        if (namesAndAliases.isEmpty()) {
            return;
        }
        String name = namesAndAliases.get(0);
        List<String> aliases = namesAndAliases.subList(1, namesAndAliases.size());
        RegisteredCommand parsed = RegisteredCommand.of(command, name, aliases);
        BukkitCommandBridge bridge = new BukkitCommandBridge(parsed, arguments, completions);
        commandMap.register(plugin.getName().toLowerCase(), bridge);
        CommandSyncer.sync();
    }

    @NotNull
    public ArgumentRegistry arguments() {
        return arguments;
    }

    @NotNull
    public CompletionRegistry completions() {
        return completions;
    }
}
