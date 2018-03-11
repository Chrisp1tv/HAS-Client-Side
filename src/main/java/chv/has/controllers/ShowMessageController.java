package chv.has.controllers;

import chv.has.utils.UserInterface.MessageInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

/**
 * @author Christopher Anciaux
 */
public class ShowMessageController {
    protected MessageInterface userInterface;

    @FXML
    protected ScrollPane messageContainer;

    @FXML
    protected Button closeForEverButton;

    public ScrollPane getMessageContainer() {
        return this.messageContainer;
    }

    public Button getCloseForEverButton() {
        return this.closeForEverButton;
    }

    public MessageInterface getUserInterface() {
        return this.userInterface;
    }

    public void setUserInterface(MessageInterface userInterface) {
        this.userInterface = userInterface;
    }

    @FXML
    protected void handleCloseMessage() {
        this.getUserInterface().close(false);
    }

    @FXML
    protected void handleCloseMessageForEver() {
        this.getUserInterface().close(true);
    }
}
