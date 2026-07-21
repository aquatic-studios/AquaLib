package com.aquaticstudios.aqualib.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class CC {

    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    @NotNull
    public String set(@NotNull String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuilder builder = new StringBuilder(input.length() + 16);
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(builder, Matcher.quoteReplacement(ChatColor.of("#" + hex).toString()));
        }
        matcher.appendTail(builder);
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }

    @NotNull
    public List<String> set(@NotNull List<String> lines) {
        return lines.stream().map(line -> set(line)).collect(Collectors.toList());
    }

    @NotNull
    public String strip(@NotNull String input) {
        String stripped = ChatColor.stripColor(set(input));
        return stripped == null ? "" : stripped;
    }
}
