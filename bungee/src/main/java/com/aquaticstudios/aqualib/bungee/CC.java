package com.aquaticstudios.aqualib.bungee;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class CC {

    private final char SECTION = '§';
    private final String CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    @NotNull
    public String set(@NotNull String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuilder builder = new StringBuilder(input.length() + 16);
        while (matcher.find()) {
            String hex = matcher.group(1).toLowerCase();
            StringBuilder replacement = new StringBuilder();
            replacement.append(SECTION).append('x');
            for (int i = 0; i < hex.length(); i++) {
                replacement.append(SECTION).append(hex.charAt(i));
            }
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement.toString()));
        }
        matcher.appendTail(builder);

        char[] chars = builder.toString().toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '&' && CODES.indexOf(chars[i + 1]) > -1) {
                chars[i] = SECTION;
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
}
