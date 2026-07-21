package com.aquaticstudios.aqualib.bungee;

import com.aquaticstudios.aqualib.config.Config;
import com.aquaticstudios.aqualib.config.ConfigFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;

public final class Configs {

    @NotNull
    public static Config versioned(@NotNull File dataFolder, @Nullable InputStream resource, @NotNull String fileName) {
        return ConfigFactory.versioned(new File(dataFolder, fileName), resource);
    }

    @NotNull
    public static Config plain(@NotNull File dataFolder, @Nullable InputStream resource, @NotNull String fileName) {
        return ConfigFactory.plain(new File(dataFolder, fileName), resource);
    }
}
