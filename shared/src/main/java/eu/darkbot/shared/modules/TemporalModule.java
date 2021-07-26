package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.managers.BotAPI;

public abstract class TemporalModule implements Module, eu.darkbot.api.extensions.TemporalModule {

    protected final BotAPI bot;
    protected final PluginAPI api;
    protected Module back;

    protected TemporalModule(PluginAPI api) {
        this.api = api;
        this.bot = api.requireAPI(BotAPI.class);
        this.back = bot.getModule();

        if (back instanceof TemporalModule)
            this.back = ((TemporalModule) this.back).back;
    }

    public void goBack() {
        this.bot.setModule(back);
        this.back = null;
    }
}
