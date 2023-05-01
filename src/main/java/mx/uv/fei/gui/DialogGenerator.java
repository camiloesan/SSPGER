package mx.uv.fei.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import mx.uv.fei.logic.AlertMessage;

public class DialogGenerator {
    public static void getDialog(AlertMessage message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (message.getAlertStatus() != null) {
            switch (message.getAlertStatus()) {
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
        alert.setContentText(message.getContent());
        alert.showAndWait();
    }
}
