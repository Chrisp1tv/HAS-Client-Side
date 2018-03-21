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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author Christopher Anciaux
 */
public class MessageInterface {
    protected static final double minPercentageOfScreenSize = 0.2;

    protected static final double medianPercentageOfScreenSize = 0.5;

    protected static final double maxPercentageOfScreenSize = 0.8;

    protected double xOffset = 0;

    protected double yOffset = 0;

    protected MessageInterfaceManager messageInterfaceManager;

    protected Stage stage;

    protected BorderPane rootElement;

    protected ShowMessageController controller;

    protected Timeline repetitionTimeline;

    protected chv.has.model.interfaces.MessageInterface message;

    MessageInterface(MessageInterfaceManager messageInterfaceManager, chv.has.model.interfaces.MessageInterface message) {
        this.messageInterfaceManager = messageInterfaceManager;
        this.message = message;
        this.setUpMessageStage();
        this.setUpController();
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

    protected void setUpMessageStage() {
        this.stage = new Stage();
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setTitle(this.messageInterfaceManager.getHas().getI18nMessages().getString("applicationTitle"));
        this.stage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));
        this.stage.setAlwaysOnTop(true);
    }

    protected void setUpController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.messageInterfaceManager.getHas().getI18nMessages());
            loader.setLocation(HAS.class.getResource("/views/show-message.fxml"));

            BorderPane view = loader.load();

            Scene scene = new Scene(view);
            scene.setFill(Color.TRANSPARENT);

            this.controller = loader.getController();
            this.controller.setUserInterface(this);

            this.rootElement = view;

            this.setUpInterface();
            this.stage.setScene(scene);
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.messageInterfaceManager.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }

    protected void setUpInterface() {
        this.setUpSizeConstraints();
        this.makeWindowDraggable();
        this.manageMessageRepetition();
        this.setUpMessageStyle();
        this.listenStageHiding();
    }

    protected void setUpSizeConstraints() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        this.rootElement.setMinHeight(visualBounds.getHeight() * MessageInterface.minPercentageOfScreenSize);
        this.rootElement.setMaxHeight(visualBounds.getHeight() * MessageInterface.maxPercentageOfScreenSize);

        this.rootElement.setMinWidth(visualBounds.getWidth() * MessageInterface.medianPercentageOfScreenSize);
        this.rootElement.setMaxWidth(visualBounds.getWidth() * MessageInterface.medianPercentageOfScreenSize);
    }

    protected void makeWindowDraggable() {
        this.rootElement.setOnMousePressed(event -> {
            this.xOffset = this.stage.getX() - event.getScreenX();
            this.yOffset = this.stage.getY() - event.getScreenY();
        });

        this.rootElement.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + this.xOffset);
            this.stage.setY(event.getScreenY() + this.yOffset);
        });
    }

    protected void manageMessageRepetition() {
        if (null == this.message.getRepetitionFrequency()) {
            this.controller.getCloseForEverButton().setVisible(false);
            this.controller.getCloseForEverButton().setManaged(false);
        } else {
            this.createRepetitionTimeline();
        }
    }

    protected void setUpMessageStyle() {
        WebView messageContainer = this.controller.getMessageContainer();
        messageContainer.getEngine().setUserStyleSheetLocation(HAS.class.getResource("/webview-styles.css").toString());
        messageContainer.getEngine().loadContent(this.getWrappedContent());

        messageContainer.getEngine().documentProperty().addListener((prop, oldDoc, newDoc) -> {
            messageContainer.setPrefHeight(Double.parseDouble(messageContainer.getEngine().executeScript("document.height").toString()));
            this.stage.show();
        });
    }

    protected String getWrappedContent() {
        return "<body style=\"width:" + (this.rootElement.getMaxWidth() - 40) + "px;\">" + this.message.getContent() + "</body>";
    }

    protected void listenStageHiding() {
        this.stage.setOnHiding(event -> {
            this.messageInterfaceManager.getHas().getRabbitMQManager().sendMessageStatus(RabbitMQManager.MESSAGE_STATUS_READ, this.message);
            this.stage.setOnHiding(null);
        });
    }

    protected void createRepetitionTimeline() {
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
