package com.aquaticstudios.aqualib.utils.reflection;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Method;

public final class CommandSyncer {

    public static void sync() {
        try {
            Server server = Bukkit.getServer();
            Method syncCommands = server.getClass().getDeclaredMethod("syncCommands");
            syncCommands.setAccessible(true);
            syncCommands.invoke(server);
        } catch (NoSuchMethodException ignored) {

        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to sync commands", exception);
        }
    }
}
