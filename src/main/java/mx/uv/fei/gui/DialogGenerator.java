package mx.uv.fei.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import mx.uv.fei.logic.AlertMessage;

public class DialogGenerator {
    public static void getDialog(AlertMessage alertMessage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (alertMessage.getAlertStatus() != null) {
            switch (alertMessage.getAlertStatus()) {
                case SUCCESS -> {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Éxito");
                    alert.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
                }
                case WARNING -> {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Advertencia");
                    alert.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
                }
                case ERROR, FATAL -> {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
                }
                default -> throw new IllegalArgumentException();
            }
        }
        alert.setTitle("");
        alert.setContentText(alertMessage.getContent());
        alert.showAndWait();
    }
    
    public static final ButtonType BUTTON_YES = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    public static final ButtonType BUTTON_NO = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    public static Optional<ButtonType> getConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, BUTTON_YES, BUTTON_NO);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Seleccione una opción");
        
        return alert.showAndWait();
    }
}
