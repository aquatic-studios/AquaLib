package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class LongParser implements ArgumentParser<Long> {

    @Override
    @NotNull
    public Long parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException exception) {
            throw new CommandException("&c'" + input + "' is not a valid number.");
        }
    }
}
