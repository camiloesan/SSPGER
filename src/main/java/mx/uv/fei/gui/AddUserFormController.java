package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.sql.SQLException;

public class AddUserFormController {
    @FXML
    private Button buttonCancel;

    @FXML
    private ComboBox<String> comboBoxUserType;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;

    private static final int MAX_FIELD_LENGTH = 15;

    private final static ObservableList<String> observableListComboItems = FXCollections.observableArrayList("administrador", "estudiante", "profesor", "representanteCA");

    @FXML
    private void buttonCancelAction() {
        closeWindow();
    }

    @FXML
    private void buttonContinueAction() {
        if (areFieldsValid()) {
            try {
                addUser();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setContentText("El usuario fue registrado exitosamente");
                alert.show();
            } catch (SQLException sqlException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Algo falló");
                alert.setContentText("Revisa el contenido de tus campos o inténtalo más tarde");
                alert.show();
            }
        }
    }

    @FXML
    private void initialize() {
        comboBoxUserType.setItems(observableListComboItems);
    }

    private boolean areFieldsValid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (textFieldUsername.getText().isBlank() || textFieldPassword.getText().isBlank() || comboBoxUserType.getValue().isBlank()) {
            alert.setTitle("Error en los campos");
            alert.show();
            return false;
        } else {
            if (textFieldUsername.getText().length() > MAX_FIELD_LENGTH || textFieldPassword.getText().length() > MAX_FIELD_LENGTH) {
                alert.setTitle("Límite de caracteres sobrepasado");
                alert.setContentText("El campo usuario y contraseña deben tener menos de " + MAX_FIELD_LENGTH + " caracteres");
                alert.show();
                return false;
            } else {
                return true;
            }
        }
    }

    private void addUser() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(textFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccountDAO.addAccessAccount(accessAccount);
    }

    private void closeWindow() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }
}
