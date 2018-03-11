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
    protected final static String DEFAULT_ICON_PATH = "/icons/icon.png";

    protected final static String DISCONNECTED_ICON_PATH = "/icons/disconnected-icon.png";

    public static void setUpSystemTray(SystemTray systemTray, ActionListener actionPerformedOnAbout,
                                       ActionListener actionPerformedOnConfiguration, ActionListener actionPerformedOnQuit,
                                       ResourceBundle i18nMessages,
                                       RabbitMQManager rabbitMQManager) {
        SwingUtil.setLookAndFeel(null);

        SystemTrayManager.setUpSystemTrayMenu(systemTray, actionPerformedOnAbout, actionPerformedOnConfiguration, actionPerformedOnQuit, i18nMessages);
        SystemTrayManager.setUpIcon(systemTray, rabbitMQManager);
    }

    protected static void setUpSystemTrayMenu(final SystemTray systemTray, ActionListener actionPerformedOnAbout,
                                              ActionListener actionPerformedOnConfiguration, ActionListener actionPerformedOnQuit,
                                              ResourceBundle i18nMessages) {
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("about"), actionPerformedOnAbout));
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("configuration"), actionPerformedOnConfiguration));
        systemTray.getMenu().add(new MenuItem(i18nMessages.getString("quit"), actionPerformedOnQuit));
    }

    protected static void setUpIcon(SystemTray systemTray, RabbitMQManager rabbitMQManager) {
        systemTray.setImage(SystemTrayManager.class.getResourceAsStream(SystemTrayManager.getIconPath(rabbitMQManager.isConnected())));

        rabbitMQManager.connectedProperty().addListener((observable, oldValue, newValue) -> systemTray.setImage(SystemTrayManager.class.getResourceAsStream(SystemTrayManager.getIconPath(newValue))));
    }

    protected static String getIconPath(boolean connected) {
        if (connected) {
            return SystemTrayManager.DEFAULT_ICON_PATH;
        }

        return SystemTrayManager.DISCONNECTED_ICON_PATH;
    }
}
