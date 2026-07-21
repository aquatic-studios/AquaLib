package com.aquaticstudios.aqualib.command.completion;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CompletionRegistry {

    private final Map<String, CompletionProvider> providers = new HashMap<>();

    public CompletionRegistry() {
        registerDefaults();
    }

    public void register(@NotNull String token, @NotNull CompletionProvider provider) {
        providers.put(normalize(token), provider);
    }

    @NotNull
    public List<String> resolve(@NotNull String token, @NotNull CommandSender sender) {
        if (token.isEmpty()) {
            return Collections.emptyList();
        }
        if (token.startsWith("@")) {
            CompletionProvider provider = providers.get(normalize(token));
            return provider == null ? Collections.emptyList() : provider.suggest(sender);
        }
        if (token.indexOf('|') >= 0) {
            return Arrays.asList(token.split("\\|"));
        }
        return Collections.singletonList(token);
    }

    private void registerDefaults() {
        register("@players", sender -> Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList()));
        register("@worlds", sender -> Bukkit.getWorlds().stream()
                .map(World::getName)
                .collect(Collectors.toList()));
        register("@empty", sender -> new ArrayList<>());
        register("@nothing", sender -> new ArrayList<>());
    }

    @NotNull
    private String normalize(@NotNull String token) {
        String lower = token.toLowerCase();
        return lower.startsWith("@") ? lower : "@" + lower;
    }
}
