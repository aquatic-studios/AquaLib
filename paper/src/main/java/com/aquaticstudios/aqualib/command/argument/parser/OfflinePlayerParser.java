package com.aquaticstudios.aqualib.command.argument.parser;

import com.aquaticstudios.aqualib.command.argument.ArgumentParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class OfflinePlayerParser implements ArgumentParser<OfflinePlayer> {

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public OfflinePlayer parse(@NotNull CommandSender sender, @NotNull String input) {
        Player online = Bukkit.getPlayerExact(input);
        return online != null ? online : Bukkit.getOfflinePlayer(input);
    }
}
