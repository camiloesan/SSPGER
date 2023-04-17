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
    private PasswordField textFieldPassword;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPreviousUsername;

    @FXML
    void buttonCancelAction() {
        Stage stage = (Stage) textFieldUsername.getScene().getWindow();
        stage.close();
    }

    private void validarDatos() {

    }

    @FXML
    void buttonContinueAction() {
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
            alert.setTitle("Error");
        }
    }

    private final static ObservableList<String> observableListComboItems = FXCollections.observableArrayList("administrador", "estudiante", "profesor", "representanteCA");

    @FXML
    private void initialize() {
        comboBoxUserType.setItems(observableListComboItems);
    }
}
