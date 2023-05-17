package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UserManagementController {
    @FXML
    private TableView<AccessAccount> tableViewAccessAccounts;
    private static String username;
    private static String userType;

    @FXML
    private void initialize() {
        TableColumn<AccessAccount, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idColumn.setMinWidth(50);
        idColumn.setMaxWidth(70);
        TableColumn<AccessAccount, String> usernameColumn = new TableColumn<>("Usuario");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<AccessAccount, String> userTypeColumn = new TableColumn<>("Tipo de usuario");
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        tableViewAccessAccounts.getColumns().addAll(idColumn, usernameColumn, userTypeColumn);
        fillTableViewAccessAccounts();
    }

    private void fillTableViewAccessAccounts() {
        UserDAO accessAccountDAO = new UserDAO();
        try {
            tableViewAccessAccounts.getItems().addAll(accessAccountDAO.getAccessAccountsList());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información de la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void openModifyUserPane() throws IOException {
        if (tableViewAccessAccounts.getSelectionModel().getSelectedItem() != null) {
            if (tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUserType().equals(LoginController.USER_ADMIN)) {
                DialogGenerator.getDialog(new AlertMessage("No se pueden modificar los usuarios administrador", AlertStatus.WARNING));
            } else {
                setUsername(tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername());
                setUserType(tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUserType());
                MainStage.changeView("panemodifyuser-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar a un usuario para modificarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void buttonDeleteAction() throws SQLException {
        if (isItemSelected()) {
            String username = tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername();
            if (isUserAdmin(username)) {
                DialogGenerator.getDialog(new AlertMessage("No se pueden eliminar los usuarios administrador", AlertStatus.WARNING));
            } else {
                deleteUser(username);
                tableViewAccessAccounts.getItems().clear();
                fillTableViewAccessAccounts();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar al usuario que quieres eliminar", AlertStatus.WARNING));
        }
    }

    private boolean isItemSelected() {
        return tableViewAccessAccounts.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void openAddUserPane() throws IOException {
        MainStage.changeView("paneadduser.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    private boolean isUserAdmin(String username) throws SQLException {
        UserDAO accessAccountDAO = new UserDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals(LoginController.USER_ADMIN);
    }

    public boolean confirmedDeleteUser(String displayUsername) {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar al usuario " + displayUsername + "?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void deleteUser(String username) throws SQLException {
        UserDAO accessAccountDAO = new UserDAO();
        if(confirmedDeleteUser(username)) {
            accessAccountDAO.deleteUserByUsername(username);
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserManagementController.username = username;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        UserManagementController.userType = userType;
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    @FXML
    private void logOut() throws IOException {
        if (confirmedLogOut()) {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
