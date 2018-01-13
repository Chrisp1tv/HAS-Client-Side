package chv.has.utils;

import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.SwingUtil;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 */
abstract public class SystemTrayManager {
    public static void setUpSystemTray(SystemTray systemTray, ActionListener actionPerformedOnAbout,
                                       ActionListener actionPerformedOnConfiguration, ActionListener actionPerformedOnQuit,
                                       ResourceBundle i18nMessages) {
        SwingUtil.setLookAndFeel(null);
        systemTray.setImage(SystemTrayManager.class.getResourceAsStream("/icons/icon.png"));

        SystemTrayManager.setUpSystemTrayMenu(systemTray, actionPerformedOnAbout, actionPerformedOnConfiguration, actionPerformedOnQuit, i18nMessages);
    }

    private static void setUpSystemTrayMenu(final SystemTray systemTray, ActionListener actionPerformedOnAbout,
                                            ActionListener actionPerformedOnConfiguration, ActionListener actionPerformedOnQuit,
                                            ResourceBundle i18nMessages) {
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("about"), actionPerformedOnAbout));
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("configuration"), actionPerformedOnConfiguration));
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("quit"), actionPerformedOnQuit));
    }
}
