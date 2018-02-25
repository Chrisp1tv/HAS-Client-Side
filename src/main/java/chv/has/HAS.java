package chv.has;

import chv.has.controllers.BaseController;
import chv.has.controllers.ConfigurationController;
import chv.has.controllers.ShowMessageController;
import chv.has.exceptions.DisconnectedException;
import chv.has.model.RabbitMQConfiguration;
import chv.has.model.interfaces.MessageInterface;
import chv.has.utils.RabbitMQConfigurationManager;
import chv.has.utils.RabbitMQManager;
import chv.has.utils.SystemTrayManager;
import dorkbox.systemTray.SystemTray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    private Stage secondaryStage;

    private SystemTray systemTray;

    private RabbitMQConfiguration RabbitMQConfiguration;

    private RabbitMQManager RabbitMQManager;

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
        this.setUpSecondaryStage();
        this.setUpSystemTray();
        this.setUpRabbitMQ();
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

    public void showSecondaryStage() {
        this.secondaryStage.show();
    }

    public void closeSecondaryStage() {
        this.secondaryStage.close();
    }

    public RabbitMQManager getRabbitMQManager() {
        return this.RabbitMQManager;
    }

    public chv.has.model.RabbitMQConfiguration getRabbitMQConfiguration() {
        return this.RabbitMQConfiguration;
    }

    public ResourceBundle getI18nMessages() {
        return this.i18nMessages;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    private void setUpPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage.setTitle(this.getI18nMessages().getString("applicationTitle"));
        this.primaryStage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));
    }

    private void setUpSecondaryStage() {
        this.secondaryStage = new Stage();
        this.secondaryStage.titleProperty().bind(this.primaryStage.titleProperty());
        this.secondaryStage.getIcons().addAll(this.primaryStage.getIcons());

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HAS.class.getResource("/views/secondary-root.fxml"));

            Scene scene = new Scene(loader.load());
            this.secondaryStage.setScene(scene);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    private void setUpSystemTray() {
        this.systemTray = SystemTray.get();
        if (null == this.systemTray) {
            throw new RuntimeException();
        }

        SystemTrayManager.setUpSystemTray(this.systemTray, this.getActionPerformedOnAbout(), this.getActionPerformedOnConfiguration(), this.getActionPerformedOnQuit(), this.getI18nMessages());
    }

    private void setUpRabbitMQ() {
        this.RabbitMQConfiguration = RabbitMQConfigurationManager.loadRabbitMQConfiguration();
        this.RabbitMQManager = new RabbitMQManager(this.getRabbitMQConfiguration());

        try {
            this.RabbitMQManager.onMessage(message -> Platform.runLater(() -> this.displayMessage(message)));
        } catch (DisconnectedException e) {
            // TODO
        }
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
                // TODO
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

        view.setOnMousePressed(event -> {
            xOffset = this.primaryStage.getX() - event.getScreenX();
            yOffset = this.primaryStage.getY() - event.getScreenY();
        });

        view.setOnMouseDragged(event -> {
            this.primaryStage.setX(event.getScreenX() + xOffset);
            this.primaryStage.setY(event.getScreenY() + yOffset);
        });
    }

    private BaseController loadViewToSecondaryStage(String viewFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(HAS.class.getResource(viewFile));
            Node view = loader.load();

            ((BorderPane) this.secondaryStage.getScene().getRoot()).setCenter(view);

            return loader.getController();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            return null;
        }
    }

    private ActionListener getActionPerformedOnAbout() {
        return e -> Platform.runLater(() -> {
            this.loadViewToSecondaryStage("/views/about.fxml");
            this.showSecondaryStage();
        });
    }

    private ActionListener getActionPerformedOnConfiguration() {
        return e -> Platform.runLater(() -> {
            ConfigurationController controller = (ConfigurationController) this.loadViewToSecondaryStage("/views/configuration.fxml");
            assert null != controller;
            controller.setHAS(this);
            controller.setUp();
            this.showSecondaryStage();
        });
    }

    private ActionListener getActionPerformedOnQuit() {
        return e -> {
            this.systemTray.shutdown();
            // TODO: find why RabbitMQManager prevents application stopping
            this.RabbitMQManager.disconnect();
            Platform.exit();
        };
    }
}