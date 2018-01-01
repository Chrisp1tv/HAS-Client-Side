package chv.has;

import chv.has.controllers.ShowMessageController;
import chv.has.model.interfaces.MessageInterface;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.SwingUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 */
public class HAS extends Application {
    private static final double maxPercentageOfScreenSize = 0.8;

    private Stage primaryStage;

    private SystemTray systemTray;

    private ResourceBundle i18nMessages;

    private ShowMessageController showMessageController;

    private LinkedList<MessageInterface> pendingMessages;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.i18nMessages = ResourceBundle.getBundle("translations/translations");
        this.setUpPrimaryStage(primaryStage);
        this.setUpSystemTray();
    }

    public void displayMessage(MessageInterface message) {
        ShowMessageController controller = this.getShowMessageController();
        controller.setMessage(message);
        controller.initializeInterface();

        this.primaryStage.show();
    }

    public void closeCurrentMessage() {
        this.primaryStage.close();
    }

    public ResourceBundle getI18nMessages() {
        return this.i18nMessages;
    }

    private void setUpPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(this.getI18nMessages().getString("applicationTitle"));
        this.primaryStage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        this.primaryStage.setMaxHeight(visualBounds.getHeight() * HAS.maxPercentageOfScreenSize);
        this.primaryStage.setMaxWidth(visualBounds.getWidth() * HAS.maxPercentageOfScreenSize);
    }

    private void setUpSystemTray() {
        this.systemTray = SystemTray.get();
        if (null == this.systemTray) {
            throw new RuntimeException();
        }

        SwingUtil.setLookAndFeel(null);
        this.systemTray.setImage(HAS.class.getResourceAsStream("/icons/icon.png"));
        this.systemTray.getMenu().add(new MenuItem(this.getI18nMessages().getString("quit"), new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                systemTray.shutdown();
                Platform.exit();
            }
        }));
    }

    private ShowMessageController getShowMessageController() {
        if (null == this.showMessageController) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setResources(this.i18nMessages);
                loader.setLocation(HAS.class.getResource("/views/show-message.fxml"));
                VBox view = loader.load();

                this.showMessageController = loader.getController();
                this.showMessageController.setHAS(this);

                this.primaryStage.setScene(new Scene(view));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.showMessageController;
    }
}