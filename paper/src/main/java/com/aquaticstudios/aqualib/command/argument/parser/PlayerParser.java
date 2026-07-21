package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import com.aquaticstudios.aqualib.command.exception.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerParser implements ArgumentParser<Player> {

    @Override
    @NotNull
    public Player parse(@NotNull CommandSender sender, @NotNull String input) throws CommandException {
        Player player = Bukkit.getPlayerExact(input);
        if (player == null) {
            throw new CommandException("&cPlayer '" + input + "' is not online.");
        }
        return player;
    }
}
