package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CRUDAccessAccountController {
    @FXML
    private ListView<String> listViewUsernames;
    @FXML
    private ComboBox<String> comboBoxUserType;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private Rectangle optionAccountsManagement;
    @FXML
    private ComboBox<String> comboBoxFilter;
    @FXML
    private ComboBox<String> comboBoxUserTypeModify;
    @FXML
    private TabPane tabPaneAccountManagement;
    @FXML
    private TextField textFieldUserToModify;
    @FXML
    private TextField textFieldNewPassword;
    private static final double SELECTED_OPACITY = 0.16;
    private final static ObservableList<String> observableListComboItemsUserType = FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsFilter = FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private static final int MAX_FIELD_LENGTH = 27;

    @FXML
    private void buttonDeleteAction() throws SQLException {
        String username = listViewUsernames.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (username == null) {
            alert.setTitle("No se puede realizar la operación");
            alert.setContentText("Debes seleccionar al usuario que quieres eliminar");
            alert.show();
        } else {
            if (isUserAdmin(username)) {
                alert.setTitle("No se puede realizar la operación");
                alert.setContentText("No se pueden eliminar los usuarios administrador");
                alert.show();
            } else {
                confirmDeletion();
                updateListView();
            }
        }
    }

    @FXML
    private void buttonModifyAction() {
        textFieldUserToModify.setText(listViewUsernames.getSelectionModel().getSelectedItem());
        tabPaneAccountManagement.getSelectionModel().select(2);
    }

    @FXML
    private void buttonConfirmModificationAction() throws SQLException {
        if (areModifyUserFieldsValid()) {
            modifyUserAttributesByUsername(textFieldUserToModify.getText(), textFieldNewPassword.getText(), comboBoxUserTypeModify.getValue());
        } else {
            //somealert
        }
    }

    @FXML
    private void buttonContinueAction() throws SQLException {
        if (areAddUserFieldsValid()) {
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
            updateListView();
        }
    }

    @FXML
    private void actionLogOut() throws IOException {
        logOut();
    }

    @FXML
    private void updateListView() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        listViewUsernames.getItems().clear();
        for(AccessAccount accessAccountObject : accessAccountDAO.getListAccessAccounts()) {
            listViewUsernames.getItems().add(accessAccountObject.getUsername());
        }
    }

    @FXML
    private void initialize() throws SQLException {
        updateListView();
        comboBoxUserType.setItems(observableListComboItemsUserType);
        optionAccountsManagement.setOpacity(SELECTED_OPACITY);
        comboBoxFilter.setItems(observableListComboItemsFilter);
        comboBoxUserTypeModify.setItems(observableListComboItemsUserType);
    }

    private boolean areAddUserFieldsValid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (textFieldUsername.getText().isBlank() || passwordFieldPassword.getText().isBlank() || comboBoxUserType.getValue().isBlank()) {
            alert.setTitle("Error en los campos");
            alert.show();
            return false;
        } else {
            if (textFieldUsername.getText().length() > MAX_FIELD_LENGTH || passwordFieldPassword.getText().length() > MAX_FIELD_LENGTH) {
                alert.setTitle("Límite de caracteres sobrepasado");
                alert.setContentText("El campo usuario y contraseña deben tener menos de " + MAX_FIELD_LENGTH + " caracteres");
                alert.show();
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean areModifyUserFieldsValid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (textFieldUserToModify.getText().isBlank() || textFieldNewPassword.getText().isBlank() || comboBoxUserTypeModify.getValue().isBlank()) {
            alert.setTitle("Error en los campos");
            alert.show();
            return false;
        } else {
            if (textFieldUserToModify.getText().length() > MAX_FIELD_LENGTH || textFieldNewPassword.getText().length() > MAX_FIELD_LENGTH) {
                alert.setTitle("Límite de caracteres sobrepasado");
                alert.setContentText("El campo usuario y contraseña deben tener menos de " + MAX_FIELD_LENGTH + " caracteres");
                alert.show();
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean isUserAdmin(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals("Administrador");
    }

    private void addUser() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccountDAO.addAccessAccount(accessAccount);
    }

    private void modifyUserAttributesByUsername(String username, String newPassword, String userType) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(username);
        accessAccount.setUserPassword(newPassword);
        accessAccount.setUserType(userType);
        accessAccountDAO.modifyPasswordByUsername(accessAccount);
    }

    private void confirmDeletion() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea eliminar al usuario " + listViewUsernames.getSelectionModel().getSelectedItem() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            accessAccountDAO.deleteAccessAccountByUsername(listViewUsernames.getSelectionModel().getSelectedItem());
        }
    }

    private void logOut() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
