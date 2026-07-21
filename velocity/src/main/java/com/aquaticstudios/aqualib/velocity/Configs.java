package com.aquaticstudios.aqualib.velocity;

import com.aquaticstudios.aqualib.config.Config;
import com.aquaticstudios.aqualib.config.ConfigFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;

public final class Configs {

    @NotNull
    public static Config versioned(@NotNull Path dataDirectory, @Nullable InputStream resource, @NotNull String fileName) {
        return ConfigFactory.versioned(dataDirectory.resolve(fileName).toFile(), resource);
    }

    @NotNull
    public static Config plain(@NotNull Path dataDirectory, @Nullable InputStream resource, @NotNull String fileName) {
        return ConfigFactory.plain(dataDirectory.resolve(fileName).toFile(), resource);
    }
}
