package chv.has;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Christopher Anciaux
 */
public class HAS extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }
}