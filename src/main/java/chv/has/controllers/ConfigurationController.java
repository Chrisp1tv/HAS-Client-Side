package chv.has.controllers;

import chv.has.utils.UserInterface.ConfigurationInterfaceManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * @author Christopher Anciaux
 */
public class ConfigurationController {
    private ConfigurationInterfaceManager configurationInterface;

    @FXML
    private TextField hostTextField;

    public void setUp() {
        if (null != this.configurationInterface) {
            this.hostTextField.setText(this.configurationInterface.getHas().getRabbitMQManager().getConfiguration().getHost());
        }
    }

    public void setConfigurationInterface(ConfigurationInterfaceManager configurationInterface) {
        this.configurationInterface = configurationInterface;
    }

    @FXML
    private void handleCancel() {
        this.configurationInterface.close();
    }

    @FXML
    private void handleConfirm() {
        if (this.hostTextField.getText().isEmpty()) {
            return;
        }

        this.configurationInterface.getHas().getRabbitMQManager().getConfiguration().setHost(this.hostTextField.getText());
        this.configurationInterface.close();
    }
}
