package chv.has.controllers;

import chv.has.model.interfaces.MessageInterface;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

/**
 * @author Christopher Anciaux
 */
public class ShowMessageController extends BaseController {
    private MessageInterface message;

    @FXML
    private ScrollPane messageContainer;

    public void initializeInterface() {
        if (null == this.message) {
            return;
        }

        this.messageContainer.setContent(new Text(this.message.getContent()));

        if (null != this.message.getColor()) {
            this.messageContainer.getContent().setStyle("-fx-fill: #" + this.message.getColor() + ";");
        }

        if (this.message.isBold()) {
            this.messageContainer.getStyleClass().add("bold");
        } else {
            this.messageContainer.getStyleClass().remove("bold");
        }
    }

    @FXML
    private void handleCloseMessage() {
        this.getHAS().closeCurrentMessage();
    }

    public void setMessage(MessageInterface message) {
        this.message = message;
    }
}
