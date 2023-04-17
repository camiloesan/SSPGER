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
        AddUserFormWindow addUserFormWindow = new AddUserFormWindow();
        Stage mainStage = (Stage) listViewUsernames.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        addUserFormWindow.start(subStage);
    }

    @FXML
    private void buttonModifyUserAction() throws IOException {
        ModifyUserFormWindow modifyUserFormWindow = new ModifyUserFormWindow();
        Stage mainStage = (Stage) listViewUsernames.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        modifyUserFormWindow.start(subStage);
    }

    private boolean isUserAdmin(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals("administrador");
    }

    @FXML
    private void buttonDeleteUserAction() throws SQLException {
        String username = listViewUsernames.getSelectionModel().getSelectedItem();
        if (username == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No se puede realizar la operación");
            alert.setContentText("Debes seleccionar al usuario que quieres eliminar");
            alert.show();
        } else {
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            //agregar confirmacion
            if (isUserAdmin(username)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No se puede realizar la operación");
                alert.setContentText("No se pueden eliminar los usuarios administrador");
                alert.show();
            } else {
                accessAccountDAO.deleteAccessAccountByUsername(username);
            }
        }
    }

    @FXML
    private void buttonCancelAction() throws IOException {
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
}
