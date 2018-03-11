package chv.has.utils.UserInterface;

import javafx.scene.control.Alert;

import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 */
abstract public class ErrorsInterfaceManager {
    protected static boolean CONNECTION_ERROR_ALREADY_DISPLAYED = false;

    public static void displayGeneralErrorAlert(ResourceBundle i18nMessages) {
        ErrorsInterfaceManager.displayErrorAlert(i18nMessages.getString("error"), i18nMessages.getString("generalError"), i18nMessages.getString("generalErrorOccurred"));
    }

    public static void displayConnectionErrorAlertIfNeeded(ResourceBundle i18nMessages) {
        if (ErrorsInterfaceManager.CONNECTION_ERROR_ALREADY_DISPLAYED) {
            return;
        }

        ErrorsInterfaceManager.CONNECTION_ERROR_ALREADY_DISPLAYED = true;
        ErrorsInterfaceManager.displayErrorAlert(i18nMessages.getString("error"), i18nMessages.getString("connectionError"), i18nMessages.getString("errorOccurredDuringConnection"));
    }

    public static void forgetConnectionError() {
        ErrorsInterfaceManager.CONNECTION_ERROR_ALREADY_DISPLAYED = false;
    }

    protected static void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.show();
    }
}
