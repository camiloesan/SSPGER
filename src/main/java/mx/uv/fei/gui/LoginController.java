package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;

import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField textFieldUser;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private void onActionButtonContinue() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        if (accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText())) {
            Stage stage = (Stage) textFieldUser.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("El usuario o contraseña no son válidos");
            alert.setContentText("Inténtelo de nuevo");
            alert.showAndWait();
        }
    }
}
