package chv.has.utils.UserInterface;

import chv.has.HAS;
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
public class AboutInterfaceManager extends AbstractUserInterfaceManager {
    private Stage stage;

    public AboutInterfaceManager(HAS has) {
        super(has);
        this.setUpStage();
    }

    public void showAbout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.getHas().getI18nMessages());
            loader.setLocation(HAS.class.getResource("/views/about.fxml"));
            Node view = loader.load();

            ((BorderPane) this.stage.getScene().getRoot()).setCenter(view);

            this.stage.show();
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }

    private void setUpStage() {
        this.stage = new Stage();
        this.stage.setTitle(this.getHas().getI18nMessages().getString("applicationTitle"));
        this.stage.getIcons().add(new Image(HAS.class.getResourceAsStream("/icons/icon.png")));

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HAS.class.getResource("/views/about-root.fxml"));

            Scene scene = new Scene(loader.load());

            this.stage.setScene(scene);
            this.stage.setResizable(false);
        } catch (IOException exception) {
            ErrorsInterfaceManager.displayGeneralErrorAlert(this.getHas().getI18nMessages());
            Logger.logException(exception);
        }
    }
}
