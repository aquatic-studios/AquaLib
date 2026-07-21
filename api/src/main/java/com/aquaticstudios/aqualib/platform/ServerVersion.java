package com.aquaticstudios.aqualib.platform;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class ServerVersion {

    private final int major;
    private final int minor;
    private final int patch;

    private ServerVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @NotNull
    public static ServerVersion current() {

        String raw = Bukkit.getBukkitVersion();
        int dash = raw.indexOf('-');
        String number = dash == -1 ? raw : raw.substring(0, dash);

        String[] parts = number.split("\\.");
        int major = parts.length > 0 ? parseOrZero(parts[0]) : 0;
        int minor = parts.length > 1 ? parseOrZero(parts[1]) : 0;
        int patch = parts.length > 2 ? parseOrZero(parts[2]) : 0;
        return new ServerVersion(major, minor, patch);
    }

    private static int parseOrZero(@NotNull String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public int major() {
        return major;
    }

    public int minor() {
        return minor;
    }

    public int patch() {
        return patch;
    }

    public boolean isAtLeast(int minor, int patch) {
        if (this.minor != minor) {
            return this.minor > minor;
        }
        return this.patch >= patch;
    }

    @Override
    @NotNull
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
