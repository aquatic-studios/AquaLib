package com.aquaticstudios.aqualib.message;

import com.aquaticstudios.aqualib.config.Config;
import com.aquaticstudios.aqualib.utils.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public final class Messages {

    private final Config lang;
    private final String prefixPath;

    @NotNull
    public String prefix() {
        return CC.set(lang.getString(prefixPath, ""));
    }

    @NotNull
    public String get(@NotNull String path, @NotNull Placeholder... placeholders) {
        String message = lang.getString(path, path);
        message = Placeholder.of("prefix", prefix()).apply(message);
        for (Placeholder placeholder : placeholders) {
            message = placeholder.apply(message);
        }
        return CC.set(message);
    }

    public void send(@NotNull CommandSender receiver, @NotNull String path, @NotNull Placeholder... placeholders) {
        String raw = lang.getString(path, path);
        if (raw.isEmpty()) {
            return;
        }
        receiver.sendMessage(get(path, placeholders));
    }

    public void sendList(@NotNull CommandSender receiver, @NotNull String path, @NotNull Placeholder... placeholders) {
        List<String> lines = lang.getStringList(path);
        if (lines == null) {
            return;
        }
        for (String line : lines) {
            line = Placeholder.of("prefix", prefix()).apply(line);
            for (Placeholder placeholder : placeholders) {
                line = placeholder.apply(line);
            }
            receiver.sendMessage(CC.set(line));
        }
    }
}
