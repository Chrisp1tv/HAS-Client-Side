package chv.has.utils.UserInterface;

import chv.has.HAS;
import chv.has.controllers.ConfigurationController;
import chv.has.utils.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Christopher Anciaux
 */
public class ConfigurationInterfaceManager extends AbstractUserInterfaceManager {
    protected ConfigurationController controller;

    protected Stage stage;

    public ConfigurationInterfaceManager(HAS has) {
        super(has);
        this.setUpStage();
    }

    public void showAbout() {
        this.show("/views/about.fxml");
    }

    public void showConfiguration() {
        this.show("/views/configuration.fxml");
        this.controller.setConfigurationInterface(this);
        this.controller.setUp();
    }

    public void close() {
        this.stage.close();
    }

    protected void show(String viewFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.getHas().getI18nMessages());
            loader.setLocation(HAS.class.getResource(viewFile));
            Node view = loader.load();

            this.controller = loader.getController();

            ((BorderPane) this.stage.getScene().getRoot()).setCenter(view);

            this.stage.close(); // In case it is already open, we close it to resize the window automatically
            this.stage.show();
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }

    protected void setUpStage() {
        this.stage = new Stage();
        this.stage.setTitle(this.getHas().getI18nMessages().getString("applicationTitle"));
        this.stage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HAS.class.getResource("/views/configuration-root.fxml"));

            Scene scene = new Scene(loader.load());

            this.stage.setScene(scene);
            this.stage.setResizable(false);
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }
}
