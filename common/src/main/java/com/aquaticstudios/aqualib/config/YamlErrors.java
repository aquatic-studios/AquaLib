package com.aquaticstudios.aqualib.config;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class YamlErrors {

    private static final Pattern MARK = Pattern.compile("line (\\d+),? column (\\d+)");

    @NotNull
    static String describe(@NotNull String fileName, @NotNull Throwable error) {
        Throwable root = deepest(error);
        String raw = message(root);

        int line = -1;
        int column = -1;
        Matcher matcher = MARK.matcher(raw);
        while (matcher.find()) {
            line = Integer.parseInt(matcher.group(1));
            column = Integer.parseInt(matcher.group(2));
        }

        String reason = reason(raw);

        if (line > 0) {
            String at = column > 0 ? "line " + line + ", column " + column : "line " + line;
            return fileName + ": YAML syntax error at " + at + " - " + reason;
        }
        return fileName + ": could not be loaded - " + reason;
    }

    @NotNull
    private static Throwable deepest(@NotNull Throwable error) {
        Throwable current = error;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }

    @NotNull
    private static String message(@NotNull Throwable error) {
        String message = error.getMessage();
        return message == null || message.isEmpty() ? error.getClass().getSimpleName() : message;
    }

    @NotNull
    private static String reason(@NotNull String raw) {
        String best = null;
        for (String line : raw.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (best == null) {
                best = trimmed;
            }
            String lower = trimmed.toLowerCase();
            if (lower.contains("expected") || lower.contains("but found") || lower.contains("not allowed")
                    || lower.contains("unexpected") || lower.contains("could not find")
                    || lower.contains("cannot start") || lower.contains("special characters")
                    || lower.contains("unclosed") || lower.contains("end of stream")) {
                best = trimmed;
                break;
            }
        }
        if (best == null) {
            best = raw.trim();
        }
        if (best.length() > 140) {
            best = best.substring(0, 137) + "...";
        }
        return best;
    }
}
