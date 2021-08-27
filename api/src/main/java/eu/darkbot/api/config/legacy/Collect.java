package eu.darkbot.api.config.legacy;

public interface Collect {

    /**
     * @return if the bot should use auto cloak when collecting
     */
    boolean getAutoCloak();

    /**
     * @return if the bot should stay away from enemies when collecting
     */
    boolean getStayAwayFromEnemies();

    /**
     * @return if the bot should make a best-effort to avoid boxes other ships are traveling towards
     */
    boolean getIgnoreContestedBoxes();

}
