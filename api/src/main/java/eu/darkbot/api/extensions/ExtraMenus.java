package eu.darkbot.api.extensions;

import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.managers.I18nAPI;
import eu.darkbot.util.gui.JTitledPopupMenuSeparator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Adds menu entries to the bot main menu for extra items
 */
public interface ExtraMenus {

    /**
     * Generate and return the components to add to the menu
     *
     * It is recommended to use the {@link I18nAPI} to get texts from
     * your resource files, not hardcoded strings. You need to pass in your plugin info.
     *
     * You can use util methods to create menu entries and submenus.
     *
     * @param api plugin api to work with
     * @return Collection of components, added to the main menu
     */
    Collection<JComponent> getExtraMenuItems(PluginAPI api);

    /**
     * If the extra menu items should automatically be put in a submenu for the plugin
     * @return true if they should be hidden in a submenu, false otherwise
     */
    default boolean autoSubmenu() {
        return false;
    }


    /**
     * Utility method to create a menu item
     * @param text shown text to the user. I18n the text by calling i18n.get(key) before calling this code
     * @param listener The action to take when pressed
     * @return the created JMenuItem with the action listener
     */
    default JMenuItem create(String text, @Nullable ActionListener listener) {
        return create(text, null, listener);
    }

    /**
     * Utility method to create a menu item
     * @param text shown text to the user. I18n the text by calling i18n.get(key) before calling this code
     * @param icon icon to show in the menu entry
     * @param listener The action to take when pressed
     * @return the created JMenuItem with the action listener
     */
    default JMenuItem create(String text, Icon icon, ActionListener listener) {
        JMenuItem item = new JMenuItem(text, icon);
        if (listener != null) item.addActionListener(listener);
        return item;
    }

    /**
     * Utility method to create a sub-menu with options inside
     * @param text shown text to the user. I18n the text by calling i18n.get(key) before calling this code
     * @param components child components to show in the inner sub-menu
     * @return the created menu
     */
    default JMenu createMenu(String text, Stream<JComponent> components) {
        JMenu menu = new JMenu(text);
        components.forEach(menu::add);
        return menu;
    }

    /**
     * Utility method to create a separator with text
     * @param text shown text to the user. I18n the text by calling i18n.get(key) before calling this code
     * @return the created separator
     */
    default JComponent createSeparator(String text) {
        return new JTitledPopupMenuSeparator(text);
    }

}
