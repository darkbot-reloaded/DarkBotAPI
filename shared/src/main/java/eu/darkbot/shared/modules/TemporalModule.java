package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Installable;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.managers.BotAPI;

public abstract class TemporalModule implements Module, eu.darkbot.api.extensions.TemporalModule, Installable {

    protected final BotAPI bot;
    protected Module back;

    protected TemporalModule(BotAPI bot) {
        this.bot = bot;
    }

    @Override
    public void install(PluginAPI api) {
        this.back = bot.getNonTemporalModule();
    }

    @Override
    public void uninstall() {}

    @Override
    public Module getBack() {
        return back;
    }

    public void goBack() {
        this.bot.setModule(back);
        this.back = null;
    }
}
