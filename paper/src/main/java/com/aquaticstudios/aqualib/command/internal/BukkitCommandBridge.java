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

        RegisteredSubcommand sub = match(args, args.length);
        if (sub == null) {
            sender.sendMessage(CC.set("&cUnknown subcommand. Try /" + command.name() + "."));
            return true;
        }

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
            callArgs = bind(sender, sub, args);
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
        int typed = args.length - 1;
        String current = args[typed].toLowerCase();
        List<String> suggestions = new ArrayList<>();

        for (RegisteredSubcommand sub : command.subcommands().values()) {
            if (!isPermitted(sender, sub) || !prefixMatches(sub, args, Math.min(typed, sub.nameWords().length))) {
                continue;
            }
            String[] path = sub.nameWords();
            if (typed < path.length) {
                String token = path[typed];
                if (isParam(token)) {
                    suggestions.addAll(completionAt(sub, paramsBefore(path, typed), sender));
                } else {
                    suggestions.add(token);
                }
            } else {
                suggestions.addAll(completionAt(sub, paramsBefore(path, path.length) + (typed - path.length), sender));
                for (CommandParameter parameter : sub.parameters()) {
                    if (parameter.isSwitch()) {
                        suggestions.add(parameter.switchFlag());
                    }
                }
            }
        }

        RegisteredSubcommand def = command.defaultSubcommand();
        if (def != null && isPermitted(sender, def)) {
            suggestions.addAll(completionAt(def, typed, sender));
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

    private RegisteredSubcommand match(@NotNull String[] args, int limit) {
        RegisteredSubcommand best = command.defaultSubcommand();
        int bestScore = best == null ? -1 : 0;
        int bestLength = 0;

        for (RegisteredSubcommand sub : command.subcommands().values()) {
            String[] path = sub.nameWords();
            if (path.length == 0 || path.length > limit) {
                continue;
            }
            int literals = 0;
            boolean ok = true;
            for (int i = 0; i < path.length; i++) {
                String token = path[i];
                if (isParam(token)) {
                    continue;
                }
                if (!args[i].equalsIgnoreCase(token)) {
                    ok = false;
                    break;
                }
                literals++;
            }
            if (ok && (literals > bestScore || (literals == bestScore && path.length > bestLength))) {
                best = sub;
                bestScore = literals;
                bestLength = path.length;
            }
        }
        return best;
    }

    @NotNull
    private Object[] bind(@NotNull CommandSender sender, @NotNull RegisteredSubcommand sub, @NotNull String[] args) {
        CommandParameter[] parameters = sub.parameters();
        Object[] call = new Object[parameters.length + 1];
        call[0] = sender;
        String[] path = sub.nameWords();

        Set<String> knownFlags = new HashSet<>();
        for (CommandParameter parameter : parameters) {
            if (parameter.isSwitch()) {
                knownFlags.add(parameter.switchFlag().toLowerCase());
            }
        }

        List<String> positional = new ArrayList<>();
        Set<String> presentFlags = new HashSet<>();
        for (int i = 0; i < path.length; i++) {
            if (isParam(path[i])) {
                positional.add(args[i]);
            }
        }
        for (int i = path.length; i < args.length; i++) {
            String token = args[i];
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
                if (parameter.hasDefault()) {
                    call[i + 1] = resolveDefault(sender, parameter);
                } else if (parameter.isOptional()) {
                    call[i + 1] = emptyValue(parameter.type());
                } else {
                    throw new CommandException(usageMessage(sub));
                }
                continue;
            }

            String token;
            if (lastPositional(parameters, i) && parameter.type() == String.class) {
                token = String.join(" ", positional.subList(index, positional.size()));
                index = positional.size();
            } else {
                token = positional.get(index);
                index++;
            }
            call[i + 1] = parseValue(sender, parameter, token);
        }
        return call;
    }

    @NotNull
    private Object parseValue(@NotNull CommandSender sender, @NotNull CommandParameter parameter, @NotNull String token) {
        Object value = arguments.parse(parameter.type(), sender, token);
        if (parameter.isRanged() && value instanceof Number) {
            validateRange((Number) value, parameter);
        }
        return value;
    }

    private Object resolveDefault(@NotNull CommandSender sender, @NotNull CommandParameter parameter) {
        String def = parameter.defaultValue();
        if ((def.equalsIgnoreCase("me") || def.equalsIgnoreCase("self")) && Player.class.isAssignableFrom(parameter.type())) {
            if (sender instanceof Player) {
                return sender;
            }
            throw new CommandException(PLAYER_ONLY);
        }
        return parseValue(sender, parameter, def);
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

    private Object emptyValue(@NotNull Class<?> type) {
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

    private boolean prefixMatches(@NotNull RegisteredSubcommand sub, @NotNull String[] args, int upto) {
        String[] path = sub.nameWords();
        for (int i = 0; i < upto; i++) {
            String token = path[i];
            if (!isParam(token) && !args[i].equalsIgnoreCase(token)) {
                return false;
            }
        }
        return true;
    }

    private int paramsBefore(@NotNull String[] path, int position) {
        int count = 0;
        for (int i = 0; i < position; i++) {
            if (isParam(path[i])) {
                count++;
            }
        }
        return count;
    }

    @NotNull
    private List<String> completionAt(@NotNull RegisteredSubcommand sub, int slot, @NotNull CommandSender sender) {
        String[] specs = sub.completions();
        if (slot < 0 || slot >= specs.length) {
            return Collections.emptyList();
        }
        return completions.resolve(specs[slot], sender);
    }

    private boolean isParam(@NotNull String token) {
        return token.length() >= 2 && token.charAt(0) == '<' && token.charAt(token.length() - 1) == '>';
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
}
