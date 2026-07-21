package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class BooleanParser implements ArgumentParser<Boolean> {

    private static final Set<String> TRUE = Set.of("true", "yes", "on", "1");
    private static final Set<String> FALSE = Set.of("false", "no", "off", "0");

    @Override
    @NotNull
    public Boolean parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException {
        String value = input.toLowerCase();
        if (TRUE.contains(value)) {
            return Boolean.TRUE;
        }
        if (FALSE.contains(value)) {
            return Boolean.FALSE;
        }
        throw new CommandException("&c'" + input + "' is not true or false.");
    }
}
