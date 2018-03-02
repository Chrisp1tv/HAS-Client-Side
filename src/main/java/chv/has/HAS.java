package chv.has;

import chv.has.exceptions.DisconnectedException;
import chv.has.model.RabbitMQConfiguration;
import chv.has.utils.RabbitMQConfigurationManager;
import chv.has.utils.RabbitMQManager;
import chv.has.utils.SystemTrayManager;
import chv.has.utils.UserInterface.ConfigurationInterfaceManager;
import chv.has.utils.UserInterface.MessageInterfaceManager;
import dorkbox.systemTray.SystemTray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 */
public class HAS extends Application {
    private SystemTray systemTray;

    private RabbitMQConfiguration rabbitMQConfiguration;

    private RabbitMQManager rabbitMQManager;

    private MessageInterfaceManager messageInterfaceManager;

    private ConfigurationInterfaceManager configurationInterfaceManager;

    private ResourceBundle i18nMessages;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);

        this.i18nMessages = ResourceBundle.getBundle("translations/translations");
        this.setUpInterface();
        this.setUpSystemTray();
        this.setUpRabbitMQ();
    }

    public RabbitMQManager getRabbitMQManager() {
        return this.rabbitMQManager;
    }

    public RabbitMQConfiguration getRabbitMQConfiguration() {
        return this.rabbitMQConfiguration;
    }

    public ResourceBundle getI18nMessages() {
        return this.i18nMessages;
    }

    private void setUpInterface() {
        this.messageInterfaceManager = new MessageInterfaceManager(this);
        this.configurationInterfaceManager = new ConfigurationInterfaceManager(this);
    }

    private void setUpSystemTray() {
        this.systemTray = SystemTray.get();
        if (null == this.systemTray) {
            throw new RuntimeException();
        }

        SystemTrayManager.setUpSystemTray(this.systemTray, this.getActionPerformedOnAbout(), this.getActionPerformedOnConfiguration(), this.getActionPerformedOnQuit(), this.getI18nMessages());
    }

    private void setUpRabbitMQ() {
        this.rabbitMQConfiguration = RabbitMQConfigurationManager.loadRabbitMQConfiguration();
        this.rabbitMQManager = new RabbitMQManager(this.getRabbitMQConfiguration());

        try {
            this.rabbitMQManager.onMessage(message -> Platform.runLater(() -> this.messageInterfaceManager.displayMessage(message)));
        } catch (DisconnectedException e) {
            // TODO
        }
    }

    private ActionListener getActionPerformedOnAbout() {
        return e -> Platform.runLater(() -> this.configurationInterfaceManager.showAbout());
    }

    private ActionListener getActionPerformedOnConfiguration() {
        return e -> Platform.runLater(() -> this.configurationInterfaceManager.showConfiguration());
    }

    private ActionListener getActionPerformedOnQuit() {
        return e -> {
            this.systemTray.shutdown();
            // TODO: find why rabbitMQManager prevents application stopping
            this.rabbitMQManager.disconnect();
            Platform.exit();
        };
    }
}