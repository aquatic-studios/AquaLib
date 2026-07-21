package com.aquaticstudios.aqualib.command.internal;

import com.aquaticstudios.aqualib.command.annotation.Completion;
import com.aquaticstudios.aqualib.command.annotation.ConsoleOnly;
import com.aquaticstudios.aqualib.command.annotation.Description;
import com.aquaticstudios.aqualib.command.annotation.Permission;
import com.aquaticstudios.aqualib.command.annotation.Subcommand;
import com.aquaticstudios.aqualib.command.annotation.Usage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class RegisteredSubcommand {

    private final Object handler;
    private final Method method;
    private final String name;
    private final String[] nameWords;
    private final String permission;
    private final String[] completions;
    private final String description;
    private final String usage;
    private final CommandParameter[] parameters;
    private final Class<?> senderType;
    private final boolean playerOnly;
    private final boolean consoleOnly;

    private RegisteredSubcommand(Object handler, Method method, String name, String[] nameWords, String permission,
                                 String[] completions, String description, String usage,
                                 CommandParameter[] parameters, Class<?> senderType,
                                 boolean playerOnly, boolean consoleOnly) {
        this.handler = handler;
        this.method = method;
        this.name = name;
        this.nameWords = nameWords;
        this.permission = permission;
        this.completions = completions;
        this.description = description;
        this.usage = usage;
        this.parameters = parameters;
        this.senderType = senderType;
        this.playerOnly = playerOnly;
        this.consoleOnly = consoleOnly;
    }

    @NotNull
    public static RegisteredSubcommand of(@NotNull Object handler, @NotNull Method method) {
        Parameter[] all = method.getParameters();
        if (all.length == 0 || !CommandSender.class.isAssignableFrom(all[0].getType())) {
            throw new IllegalArgumentException("Subcommand " + method.getName()
                    + " must take a CommandSender or Player as its first parameter.");
        }
        Class<?> senderType = all[0].getType();
        boolean playerOnly = Player.class.isAssignableFrom(senderType);
        boolean consoleOnly = ConsoleCommandSender.class.isAssignableFrom(senderType)
                || method.isAnnotationPresent(ConsoleOnly.class)
                || handler.getClass().isAnnotationPresent(ConsoleOnly.class);

        CommandParameter[] parameters = new CommandParameter[all.length - 1];
        for (int i = 1; i < all.length; i++) {
            parameters[i - 1] = CommandParameter.of(all[i]);
        }

        Subcommand subcommand = method.getAnnotation(Subcommand.class);
        Permission permission = method.getAnnotation(Permission.class);
        Completion completion = method.getAnnotation(Completion.class);
        Description description = method.getAnnotation(Description.class);
        Usage usage = method.getAnnotation(Usage.class);

        String name = subcommand.value().trim().toLowerCase();
        String[] words = name.isEmpty() ? new String[0] : name.split("\\s+");

        method.setAccessible(true);
        return new RegisteredSubcommand(
                handler,
                method,
                name,
                words,
                permission == null ? "" : permission.value(),
                completion == null ? new String[0] : completion.value(),
                description == null ? "" : description.value(),
                usage == null ? "" : usage.value(),
                parameters,
                senderType,
                playerOnly,
                consoleOnly
        );
    }

    public void invoke(@NotNull Object[] arguments) throws InvocationTargetException, IllegalAccessException {
        method.invoke(handler, arguments);
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public String[] nameWords() {
        return nameWords;
    }

    public boolean isDefault() {
        return name.isEmpty();
    }

    @NotNull
    public String permission() {
        return permission;
    }

    @NotNull
    public String[] completions() {
        return completions;
    }

    @NotNull
    public String description() {
        return description;
    }

    @NotNull
    public String usage() {
        return usage;
    }

    @NotNull
    public CommandParameter[] parameters() {
        return parameters;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public boolean isConsoleOnly() {
        return consoleOnly;
    }

    @NotNull
    public Class<?> senderType() {
        return senderType;
    }
}
