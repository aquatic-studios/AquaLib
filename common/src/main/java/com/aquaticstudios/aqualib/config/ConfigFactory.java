package com.aquaticstudios.aqualib.config;

import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;

public final class ConfigFactory {

    private static final String DEFAULT_VERSION_PATH = "version";

    @NotNull
    public static Config versioned(@NotNull File file, @Nullable InputStream resource) {
        return new Config(
                file,
                resource,
                GeneralSettings.builder().setUseDefaults(false).build(),
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning(DEFAULT_VERSION_PATH)).build()
        );
    }

    @NotNull
    public static Config plain(@NotNull File file, @Nullable InputStream resource) {
        return new Config(
                file,
                resource,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.DEFAULT
        );
    }
}
