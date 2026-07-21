package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class StringParser implements ArgumentParser<String> {

    @Override
    @NotNull
    public String parse(@NotNull CommandSender sender, @NotNull String input) {
        return input;
    }
}
