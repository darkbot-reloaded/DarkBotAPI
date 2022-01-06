package eu.darkbot.api.extensions;

import java.util.Set;

public interface IssueHandler {

    Set<? extends Issue> getIssueSet();

    void addInfo(String message, String description);
    void addWarning(String message, String description);
    void addFailure(String message, String description);

    interface Issue {

        String getMessageKey();
        String getDescription();
        Level getIssueLevel();
    }

    enum Level {
        INFO, WARNING, ERROR
    }
}
