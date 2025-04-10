package eu.darkbot.impl.utils;

import eu.darkbot.api.managers.GameResourcesAPI;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationMatcherImpl implements GameResourcesAPI.TranslationMatcher {
    private static final Pattern ESCAPE_SPECIAL_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");
    private static final Pattern REPLACE_UNUSED = Pattern.compile("%.*?%");

    @Getter
    private final Matcher matcher;
    private final int amount;

    @Getter
    private final Locale language;

    public TranslationMatcherImpl(Locale language, String translation, String... replacements) {
        this.language = Objects.requireNonNull(language);

        String s = ESCAPE_SPECIAL_CHARS.matcher(translation).replaceAll("\\\\$0");
        int amount = 0;
        for (String replacement : replacements) {
            s = s.replace(replacement, "(?<n" + ++amount + ">.*)");
        }

        this.matcher = Pattern.compile(REPLACE_UNUSED.matcher(s).replaceAll("(.*)")).matcher("");
        this.amount = amount;
    }

    @Override
    public boolean find(String log) {
        matcher.reset(log);
        return matcher.find();
    }

    @Override
    public @Nullable String get(int group) {
        if (group < 0 || group > amount) throw new IndexOutOfBoundsException("Index out of bounds");
        if (group == 0) return matcher.group();
        return matcher.group("n" + group);
    }
}
