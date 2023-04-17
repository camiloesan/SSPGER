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

    private final static ObservableList<String> observableListComboItems = FXCollections.observableArrayList("administrador", "estudiante", "profesor", "representanteCA");
    @FXML
    private void buttonCancelAction() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }
    private void validarDatos() {

    }
    @FXML
    private void buttonContinueAction() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(textFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        try {
            accessAccountDAO.addAccessAccount(accessAccount);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setContentText("El usuario fue registrado exitosamente");
            alert.show();
        } catch (SQLException sqlException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Algo falló");
            alert.setContentText("Revisa el contenido de tus campos o inténtalo más tarde");
        }
    }
    @FXML
    private void initialize() {
        comboBoxUserType.setItems(observableListComboItems);
    }
}
