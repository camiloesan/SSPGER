package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CRUDAccessAccountController {
    @FXML
    private ListView<String> listViewUsernames;

    @FXML
    private void buttonAddNewUserAction() throws IOException {
        startAddUserWindow();
    }

    @FXML
    private void buttonModifyUserAction() throws IOException {
        startModifyUserWindow();
    }

    @FXML
    private void buttonDeleteUserAction() throws SQLException {
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
    private void buttonCancelAction() throws IOException {
        returnToPreviousWindow();
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
    }

    private void startAddUserWindow() throws IOException {
        AddUserFormWindow addUserFormWindow = new AddUserFormWindow();
        Stage mainStage = (Stage) listViewUsernames.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        addUserFormWindow.start(subStage);
    }

    private void startModifyUserWindow() throws IOException {
        ModifyUserFormWindow modifyUserFormWindow = new ModifyUserFormWindow();
        Stage mainStage = (Stage) listViewUsernames.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        modifyUserFormWindow.start(subStage);
    }

    private boolean isUserAdmin(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals("Administrador");
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

    private void returnToPreviousWindow() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        LoginWindow loginWindow = new LoginWindow();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            loginWindow.start(new Stage());
            Stage stage = (Stage) listViewUsernames.getScene().getWindow();
            stage.close();
        }
    }
}
