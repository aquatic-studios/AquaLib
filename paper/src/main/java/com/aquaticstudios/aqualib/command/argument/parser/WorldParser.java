package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class WorldParser implements ArgumentParser<World> {

    @Override
    @NotNull
    public World parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException {
        World world = Bukkit.getWorld(input);
        if (world == null) {
            throw new CommandException("&cWorld '" + input + "' does not exist.");
        }
        return world;
    }
}
