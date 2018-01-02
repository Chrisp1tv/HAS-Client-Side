package chv.has;

import chv.has.controllers.ShowMessageController;
import chv.has.model.Message;
import chv.has.model.interfaces.MessageInterface;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.SwingUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 */
public class HAS extends Application {
    private static final double minPercentageOfScreenSize = 0.2;

    private static final double maxPercentageOfScreenSize = 0.8;

    private static double xOffset = 0;

    private static double yOffset = 0;

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
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage.setTitle(this.getI18nMessages().getString("applicationTitle"));
        this.primaryStage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));
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

                BorderPane view = loader.load();
                this.setUpView(view);

                Scene scene = new Scene(view);
                scene.setFill(Color.TRANSPARENT);

                this.showMessageController = loader.getController();
                this.showMessageController.setHAS(this);

                this.primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.showMessageController;
    }

    private void setUpView(BorderPane view) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        view.setMinHeight(visualBounds.getHeight() * HAS.minPercentageOfScreenSize);
        view.setMinWidth(visualBounds.getWidth() * HAS.minPercentageOfScreenSize);
        view.setMaxHeight(visualBounds.getHeight() * HAS.maxPercentageOfScreenSize);
        view.setMaxWidth(visualBounds.getWidth() * HAS.maxPercentageOfScreenSize);

        view.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });

        view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
    }
}