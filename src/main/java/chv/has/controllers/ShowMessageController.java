package chv.has.controllers;

import chv.has.utils.UserInterface.MessageInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

/**
 * @author Christopher Anciaux
 */
public class ShowMessageController {
    private MessageInterface userInterface;

    @FXML
    private ScrollPane messageContainer;

    @FXML
    private Button closeForEverButton;

    @FXML
    private void handleCloseMessage() {
        this.getUserInterface().close(false);
    }

    @FXML
    private void handleCloseMessageForEver() {
        this.getUserInterface().close(true);
    }

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
}
