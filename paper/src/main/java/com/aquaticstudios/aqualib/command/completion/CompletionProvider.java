package com.aquaticstudios.aqualib.command.completion;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface CompletionProvider {

    @NotNull
    List<String> suggest(@NotNull CommandSender sender);
}
