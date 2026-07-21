package com.aquaticstudios.aqualib.platform;

import org.jetbrains.annotations.NotNull;

public final class PlatformProvider {

    private static Platform platform;
    private static ServerVersion version;

    @NotNull
    public static Platform platform() {
        if (platform == null) {
            platform = detectPlatform();
        }
        return platform;
    }

    @NotNull
    public static ServerVersion version() {
        if (version == null) {
            version = ServerVersion.current();
        }
        return version;
    }

    @NotNull
    private static Platform detectPlatform() {
        if (classExists("io.papermc.paper.threadedregions.RegionizedServer")) {
            return Platform.FOLIA;
        }
        if (classExists("com.destroystokyo.paper.PaperConfig")
                || classExists("io.papermc.paper.configuration.Configuration")) {
            return Platform.PAPER;
        }
        return Platform.SPIGOT;
    }

    private static boolean classExists(@NotNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}
