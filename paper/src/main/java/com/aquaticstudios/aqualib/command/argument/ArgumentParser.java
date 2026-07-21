package com.aquaticstudios.aqualib.command.argument;

import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ArgumentParser<T> {

    T parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException;
}
