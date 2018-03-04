package chv.has.utils.UserInterface;

import chv.has.HAS;
import chv.has.controllers.ShowMessageController;
import chv.has.utils.Logger;
import chv.has.utils.RabbitMQManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Christopher Anciaux
 */
public class MessageInterface {
    private static final double minPercentageOfScreenSize = 0.2;

    private static final double maxPercentageOfScreenSize = 0.8;

    private double xOffset = 0;

    private double yOffset = 0;

    private MessageInterfaceManager messageInterfaceManager;

    private Stage stage;

    private ShowMessageController controller;

    private Timeline repetitionTimeline;

    private chv.has.model.interfaces.MessageInterface message;

    MessageInterface(MessageInterfaceManager messageInterfaceManager, chv.has.model.interfaces.MessageInterface message) {
        this.messageInterfaceManager = messageInterfaceManager;
        this.message = message;
        this.setUpMessageStage();
        this.setUpController();
        this.stage.show();
    }

    public void close(boolean forEver) {
        this.stage.close();

        if (forEver || null == this.message.getRepetitionFrequency()) {
            if (null != this.repetitionTimeline) {
                this.repetitionTimeline.stop();
            }

            this.messageInterfaceManager.forgetMessage(this.message);
        }
    }

    private void setUpMessageStage() {
        this.stage = new Stage();
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setTitle(this.messageInterfaceManager.getHas().getI18nMessages().getString("applicationTitle"));
        this.stage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));
        this.stage.setAlwaysOnTop(true);
    }

    private void setUpController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.messageInterfaceManager.getHas().getI18nMessages());
            loader.setLocation(HAS.class.getResource("/views/show-message.fxml"));

            BorderPane view = loader.load();

            Scene scene = new Scene(view);
            scene.setFill(Color.TRANSPARENT);

            this.controller = loader.getController();
            this.controller.setUserInterface(this);

            this.setUpInterface(view);
            this.stage.setScene(scene);
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.messageInterfaceManager.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }

    private void setUpInterface(BorderPane view) {
        this.setUpSizeConstraints(view);
        this.makeWindowDraggable(view);
        this.manageMessageRepetition();
        this.setUpMessageStyle();
        this.listenStageHiding();
    }

    private void setUpSizeConstraints(BorderPane view) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        view.setMinHeight(visualBounds.getHeight() * MessageInterface.minPercentageOfScreenSize);
        view.setMinWidth(visualBounds.getWidth() * MessageInterface.minPercentageOfScreenSize);
        view.setMaxHeight(visualBounds.getHeight() * MessageInterface.maxPercentageOfScreenSize);
        view.setMaxWidth(visualBounds.getWidth() * MessageInterface.maxPercentageOfScreenSize);
    }

    private void makeWindowDraggable(BorderPane view) {
        view.setOnMousePressed(event -> {
            this.xOffset = this.stage.getX() - event.getScreenX();
            this.yOffset = this.stage.getY() - event.getScreenY();
        });

        view.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + xOffset);
            this.stage.setY(event.getScreenY() + yOffset);
        });
    }

    private void manageMessageRepetition() {
        if (null == this.message.getRepetitionFrequency()) {
            this.controller.getCloseForEverButton().setVisible(false);
            this.controller.getCloseForEverButton().setManaged(false);
        } else {
            this.createRepetitionTimeline();
        }
    }

    private void setUpMessageStyle(){
        ScrollPane messageContainer = this.controller.getMessageContainer();
        Text messageText = new Text(this.message.getContent());

        messageContainer.widthProperty().addListener((observable, oldValue, newValue) -> messageText.setWrappingWidth(newValue.intValue() - 15));
        messageContainer.setContent(messageText);

        if (null != this.message.getColor()) {
            messageContainer.getContent().setStyle("-fx-fill: " + this.message.getColor() + ";");
        }

        if (this.message.isBold()) {
            messageContainer.getStyleClass().add("bold");
        } else {
            messageContainer.getStyleClass().remove("bold");
        }
    }

    private void listenStageHiding() {
        this.stage.setOnHiding(event -> {
            this.messageInterfaceManager.getHas().getRabbitMQManager().sendMessageStatus(RabbitMQManager.MESSAGE_STATUS_READ, this.message);
            this.stage.setOnHiding(null);
        });
    }

    private void createRepetitionTimeline() {
        this.repetitionTimeline = new Timeline(new KeyFrame(Duration.minutes(this.message.getRepetitionFrequency()), event -> {
            if (this.message.isEnded()) {
                this.repetitionTimeline.stop();
            } else {
                this.stage.show();
            }
        }));

        this.repetitionTimeline.play();
    }
}
