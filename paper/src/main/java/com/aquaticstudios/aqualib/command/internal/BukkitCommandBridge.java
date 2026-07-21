package com.aquaticstudios.aqualib.command.internal;

import com.aquaticstudios.aqualib.command.argument.ArgumentRegistry;
import com.aquaticstudios.aqualib.command.completion.CompletionRegistry;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import com.aquaticstudios.aqualib.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BukkitCommandBridge extends org.bukkit.command.Command {

    private static final String NO_PERMISSION = "&cYou don't have permission to use this.";
    private static final String PLAYER_ONLY = "&cOnly players can use this command.";
    private static final String CONSOLE_ONLY = "&cThis command can only be used from the console.";

    private final RegisteredCommand command;
    private final ArgumentRegistry arguments;
    private final CompletionRegistry completions;

    public BukkitCommandBridge(@NotNull RegisteredCommand command,
                               @NotNull ArgumentRegistry arguments,
                               @NotNull CompletionRegistry completions) {
        super(command.name(), command.description(), "/" + command.name(), command.aliases());
        this.command = command;
        this.arguments = arguments;
        this.completions = completions;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!command.permission().isEmpty() && !sender.hasPermission(command.permission())) {
            sender.sendMessage(CC.set(NO_PERMISSION));
            return true;
        }

        Match match = match(args, args.length);
        RegisteredSubcommand sub = match.sub;
        if (sub == null) {
            sender.sendMessage(CC.set("&cUnknown subcommand. Try /" + command.name() + "."));
            return true;
        }
        String[] subArgs = Arrays.copyOfRange(args, match.words, args.length);

        if (!sub.permission().isEmpty() && !sender.hasPermission(sub.permission())) {
            sender.sendMessage(CC.set(NO_PERMISSION));
            return true;
        }
        if (sub.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(CC.set(PLAYER_ONLY));
            return true;
        }
        if (sub.isConsoleOnly() && sender instanceof Player) {
            sender.sendMessage(CC.set(CONSOLE_ONLY));
            return true;
        }
        if (!sub.senderType().isInstance(sender)) {
            sender.sendMessage(CC.set(sub.isPlayerOnly() ? PLAYER_ONLY : CONSOLE_ONLY));
            return true;
        }

        Object[] callArgs;
        try {
            callArgs = bind(sender, sub, subArgs);
        } catch (CommandException exception) {
            sender.sendMessage(CC.set(exception.getMessage()));
            return true;
        }

        try {
            sub.invoke(callArgs);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof CommandException) {
                sender.sendMessage(CC.set(cause.getMessage()));
            } else {
                sender.sendMessage(CC.set("&cAn error occurred while running this command."));
                (cause == null ? exception : cause).printStackTrace();
            }
        } catch (IllegalAccessException exception) {
            sender.sendMessage(CC.set("&cAn error occurred while running this command."));
        }
        return true;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        int typedWords = args.length - 1;
        String current = args[typedWords].toLowerCase();
        List<String> suggestions = new ArrayList<>();

        for (RegisteredSubcommand sub : command.subcommands().values()) {
            if (!isPermitted(sender, sub)) {
                continue;
            }
            String[] words = sub.nameWords();
            if (words.length <= typedWords) {
                continue;
            }
            boolean prefix = true;
            for (int i = 0; i < typedWords; i++) {
                if (!args[i].equalsIgnoreCase(words[i])) {
                    prefix = false;
                    break;
                }
            }
            if (prefix) {
                suggestions.add(words[typedWords]);
            }
        }

        Match match = match(args, typedWords);
        RegisteredSubcommand sub = match.sub;
        if (sub != null && isPermitted(sender, sub)) {
            suggestions.addAll(completionAt(sub, typedWords - match.words, sender));
            for (CommandParameter parameter : sub.parameters()) {
                if (parameter.isSwitch()) {
                    suggestions.add(parameter.switchFlag());
                }
            }
        }

        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().startsWith(current)) {
                filtered.add(suggestion);
            }
        }
        Collections.sort(filtered);
        return filtered;
    }

    @NotNull
    private Match match(@NotNull String[] args, int limit) {
        RegisteredSubcommand best = command.defaultSubcommand();
        int bestWords = 0;
        for (RegisteredSubcommand sub : command.subcommands().values()) {
            String[] words = sub.nameWords();
            if (words.length == 0 || words.length > limit) {
                continue;
            }
            boolean ok = true;
            for (int i = 0; i < words.length; i++) {
                if (!args[i].equalsIgnoreCase(words[i])) {
                    ok = false;
                    break;
                }
            }
            if (ok && words.length > bestWords) {
                best = sub;
                bestWords = words.length;
            }
        }
        return new Match(best, bestWords);
    }

    @NotNull
    private Object[] bind(@NotNull CommandSender sender, @NotNull RegisteredSubcommand sub, @NotNull String[] args) {
        CommandParameter[] parameters = sub.parameters();
        Object[] call = new Object[parameters.length + 1];
        call[0] = sender;

        Set<String> knownFlags = new HashSet<>();
        for (CommandParameter parameter : parameters) {
            if (parameter.isSwitch()) {
                knownFlags.add(parameter.switchFlag().toLowerCase());
            }
        }

        List<String> positional = new ArrayList<>();
        Set<String> presentFlags = new HashSet<>();
        for (String token : args) {
            if (knownFlags.contains(token.toLowerCase())) {
                presentFlags.add(token.toLowerCase());
            } else {
                positional.add(token);
            }
        }

        int index = 0;
        for (int i = 0; i < parameters.length; i++) {
            CommandParameter parameter = parameters[i];

            if (parameter.isSwitch()) {
                call[i + 1] = presentFlags.contains(parameter.switchFlag().toLowerCase());
                continue;
            }
            if (parameter.type() == String[].class) {
                call[i + 1] = positional.subList(index, positional.size()).toArray(new String[0]);
                index = positional.size();
                continue;
            }
            if (index >= positional.size()) {
                if (parameter.isOptional()) {
                    call[i + 1] = defaultValue(parameter.type());
                    continue;
                }
                throw new CommandException(usageMessage(sub));
            }

            String token;
            if (lastPositional(parameters, i) && parameter.type() == String.class) {
                token = String.join(" ", positional.subList(index, positional.size()));
                index = positional.size();
            } else {
                token = positional.get(index);
                index++;
            }

            Object value = arguments.parse(parameter.type(), sender, token);
            if (parameter.isRanged() && value instanceof Number) {
                validateRange((Number) value, parameter);
            }
            call[i + 1] = value;
        }
        return call;
    }

    private boolean lastPositional(@NotNull CommandParameter[] parameters, int index) {
        for (int i = index + 1; i < parameters.length; i++) {
            if (!parameters[i].isSwitch()) {
                return false;
            }
        }
        return true;
    }

    private void validateRange(@NotNull Number value, @NotNull CommandParameter parameter) {
        double actual = value.doubleValue();
        if (actual < parameter.min() || actual > parameter.max()) {
            throw new CommandException(rangeMessage(parameter));
        }
    }

    @NotNull
    private String rangeMessage(@NotNull CommandParameter parameter) {
        boolean hasMin = parameter.min() != Double.NEGATIVE_INFINITY;
        boolean hasMax = parameter.max() != Double.POSITIVE_INFINITY;
        if (hasMin && hasMax) {
            return "&cThe value must be between " + number(parameter.min()) + " and " + number(parameter.max()) + ".";
        }
        if (hasMin) {
            return "&cThe value must be at least " + number(parameter.min()) + ".";
        }
        if (hasMax) {
            return "&cThe value must be at most " + number(parameter.max()) + ".";
        }
        return "&cThe value is out of range.";
    }

    @NotNull
    private String number(double value) {
        if (!Double.isInfinite(value) && value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private Object defaultValue(@NotNull Class<?> type) {
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == double.class) {
            return 0.0d;
        }
        if (type == float.class) {
            return 0.0f;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == char.class) {
            return '\0';
        }
        return null;
    }

    @NotNull
    private List<String> completionAt(@NotNull RegisteredSubcommand sub, int index, @NotNull CommandSender sender) {
        String[] specs = sub.completions();
        if (index < 0 || index >= specs.length) {
            return Collections.emptyList();
        }
        return completions.resolve(specs[index], sender);
    }

    private boolean isPermitted(@NotNull CommandSender sender, @NotNull RegisteredSubcommand sub) {
        return sub.permission().isEmpty() || sender.hasPermission(sub.permission());
    }

    @NotNull
    private String usageMessage(@NotNull RegisteredSubcommand sub) {
        StringBuilder builder = new StringBuilder("&cUsage: /").append(command.name());
        if (!sub.isDefault()) {
            builder.append(' ').append(sub.name());
        }
        if (!sub.usage().isEmpty()) {
            builder.append(' ').append(sub.usage());
        }
        return builder.toString();
    }

    private static final class Match {

        private final RegisteredSubcommand sub;
        private final int words;

        private Match(RegisteredSubcommand sub, int words) {
            this.sub = sub;
            this.words = words;
        }
    }
}
