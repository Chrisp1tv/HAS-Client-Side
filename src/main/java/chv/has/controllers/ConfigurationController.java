package chv.has.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * @author Christopher Anciaux
 */
public class ConfigurationController extends BaseController {
    @FXML
    private TextField hostTextField;

    public void setUp() {
        if (null != this.getHAS()) {
            this.hostTextField.setText(this.getHAS().getRabbitMQManager().getConfiguration().getHost());
        }
    }

    @FXML
    private void handleCancel() {
        this.getHAS().closeSecondaryStage();
    }

    @FXML
    private void handleConfirm() {
        if (this.hostTextField.getText().isEmpty()) {
            return;
        }

        this.getHAS().getRabbitMQManager().getConfiguration().setHost(this.hostTextField.getText());
        this.getHAS().closeSecondaryStage();
    }
}
