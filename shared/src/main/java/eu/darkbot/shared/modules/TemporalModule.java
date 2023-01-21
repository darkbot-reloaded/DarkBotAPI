package eu.darkbot.shared.modules;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.extensions.Installable;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.managers.BotAPI;

import java.util.Objects;

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
    public Module getBack() {
        return back;
    }

    @Override
    public void goBack() {
        // We're no longer in control, do nothing!
        // This may happen if a config setting is triggering it.
        if (!Objects.equals(bot.getModule(), this)) return;
        this.bot.setModule(back);
        this.back = null;
    }
}
