package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.sql.SQLException;

public class ModifyUserFormController {
    @FXML
    private ComboBox<String> comboBoxUserType;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldPreviousUsername;

    private final static int MAX_FIELD_LENGTH = 15;

    @FXML
    void buttonCancelAction() {
        closeCurrentWindow();
    }

    @FXML
    void buttonContinueAction() {
        if (areFieldsValid()) {
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUsername(textFieldUsername.getText());
            accessAccount.setUserPassword(textFieldPassword.getText());
            accessAccount.setUserType(comboBoxUserType.getValue());
            try {
                accessAccountDAO.modifyAccessAccountByUsername(textFieldPreviousUsername.getText(), accessAccount);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modificado satisfactoriamente");
            } catch(SQLException sqlException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error con la base de datos");
            }
        }
    }

    private final static ObservableList<String> observableListComboItems = FXCollections.observableArrayList("administrador", "estudiante", "profesor", "representanteCA");

    @FXML
    private void initialize() {
        comboBoxUserType.setItems(observableListComboItems);
    }

    private boolean areFieldsValid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (textFieldPreviousUsername.getText().isBlank() || textFieldUsername.getText().isBlank() || textFieldPassword.getText().isBlank() || comboBoxUserType.getValue().isBlank()) {
            alert.setTitle("Los campos no deben estar vacíos");
            alert.show();
            return false;
        } else {
            if (textFieldPreviousUsername.getText().length() > MAX_FIELD_LENGTH || textFieldUsername.getText().length() > MAX_FIELD_LENGTH || textFieldPassword.getText().length() > MAX_FIELD_LENGTH) {
                alert.setTitle("Límite de caracteres sobrepasado");
                alert.setContentText("El campo usuario y contraseña deben tener menos de " + MAX_FIELD_LENGTH + " caracteres");
                alert.show();
                return false;
            } else {
                return true;
            }
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) textFieldUsername.getScene().getWindow();
        stage.close();
    }
}
