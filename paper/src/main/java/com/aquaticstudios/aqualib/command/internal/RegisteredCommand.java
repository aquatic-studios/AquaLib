package com.aquaticstudios.aqualib.command.internal;

import com.aquaticstudios.aqualib.command.BaseCommand;
import com.aquaticstudios.aqualib.command.annotation.Command;
import com.aquaticstudios.aqualib.command.annotation.Subcommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RegisteredCommand {

    private final String name;
    private final List<String> aliases;
    private final String permission;
    private final String description;
    private final Map<String, RegisteredSubcommand> subcommands;
    private final RegisteredSubcommand defaultSubcommand;

    private RegisteredCommand(String name, List<String> aliases, String permission, String description,
                              Map<String, RegisteredSubcommand> subcommands, RegisteredSubcommand defaultSubcommand) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.description = description;
        this.subcommands = subcommands;
        this.defaultSubcommand = defaultSubcommand;
    }

    @NotNull
    public static RegisteredCommand of(@NotNull BaseCommand command) {
        Command annotation = command.getClass().getAnnotation(Command.class);
        if (annotation == null) {
            throw new IllegalArgumentException(command.getClass().getSimpleName()
                    + " is missing the @Command annotation.");
        }
        return build(command, annotation.value().toLowerCase(),
                new ArrayList<>(Arrays.asList(annotation.aliases())),
                annotation.permission(), annotation.description());
    }

    @NotNull
    public static RegisteredCommand of(@NotNull BaseCommand command, @NotNull String name, @NotNull List<String> aliases) {
        Command annotation = command.getClass().getAnnotation(Command.class);
        String permission = annotation == null ? "" : annotation.permission();
        String description = annotation == null ? "" : annotation.description();
        return build(command, name.toLowerCase(), new ArrayList<>(aliases), permission, description);
    }

    @NotNull
    private static RegisteredCommand build(@NotNull BaseCommand command, @NotNull String name,
                                           @NotNull List<String> aliases, @NotNull String permission,
                                           @NotNull String description) {
        Map<String, RegisteredSubcommand> subcommands = new HashMap<>();
        RegisteredSubcommand defaultSubcommand = null;

        for (Method method : command.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subcommand.class)) {
                continue;
            }
            RegisteredSubcommand sub = RegisteredSubcommand.of(command, method);
            if (sub.isDefault()) {
                defaultSubcommand = sub;
            } else {
                subcommands.put(sub.name(), sub);
            }
        }

        return new RegisteredCommand(name, aliases, permission, description, subcommands, defaultSubcommand);
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public List<String> aliases() {
        return aliases;
    }

    @NotNull
    public String permission() {
        return permission;
    }

    @NotNull
    public String description() {
        return description;
    }

    @NotNull
    public Map<String, RegisteredSubcommand> subcommands() {
        return subcommands;
    }

    @Nullable
    public RegisteredSubcommand defaultSubcommand() {
        return defaultSubcommand;
    }
}
