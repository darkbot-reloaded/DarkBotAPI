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
        this.back = bot.getModule();

        if (back instanceof TemporalModule)
            this.back = ((TemporalModule) this.back).back;
    }

    @Override
    public void uninstall() {}

    public void goBack() {
        this.bot.setModule(back);
        this.back = null;
    }
}
