package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class DoubleParser implements ArgumentParser<Double> {

    @Override
    @NotNull
    public Double parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException exception) {
            throw new CommandException("&c'" + input + "' is not a valid decimal.");
        }
    }
}
