package eu.darkbot.api.extensions;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IssueHandler {

    @NotNull Set<? extends Issue> getIssueSet();

    void addIssue(@NotNull String message, @NotNull String description, @NotNull Level level);

    default void addInfo(@NotNull String message, @NotNull String description) {
        addIssue(message, description, Level.INFO);
    }

    default void addWarning(@NotNull String message, @NotNull String description) {
        addIssue(message, description, Level.WARNING);

    }

    default void addFailure(@NotNull String message, @NotNull String description) {
        addIssue(message, description, Level.ERROR);
    }

    interface Issue {
        String getMessageKey();

        String getDescription();

        Level getIssueLevel();
    }

    enum Level {
        INFO, WARNING, ERROR
    }
}
