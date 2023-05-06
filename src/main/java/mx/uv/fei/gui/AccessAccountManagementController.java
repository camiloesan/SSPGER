package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.AccessAccountDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AccessAccountManagementController {
    @FXML
    private ComboBox<String> comboBoxUserType;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private ComboBox<String> comboBoxUserTypeToModify;
    @FXML
    private TabPane tabPaneAccountManagement;
    @FXML
    private TextField textFieldUserToModify;
    @FXML
    private TextField textFieldNewPassword;
    @FXML
    private GridPane gridPaneProfessor;
    @FXML
    private GridPane gridPaneStudent;
    @FXML
    private GridPane gridPaneNewProfessor;
    @FXML
    private GridPane gridPaneNewStudent;
    @FXML
    private ComboBox<String> comboBoxDegree;
    @FXML
    private TextField textFieldStudentId;
    @FXML
    private TextField textFieldStudentName;
    @FXML
    private TextField textFieldStudentLastName;
    @FXML
    private TextField textFieldStudentEmail;
    @FXML
    private TextField textFieldProfessorName;
    @FXML
    private TextField textFieldProfessorLastName;
    @FXML
    private TextField textFieldProfessorEmail;
    @FXML
    private TextField textFieldNewUsername;
    @FXML
    private TextField textFieldNewStudentId;
    @FXML
    private TextField textFieldNewStudentName;
    @FXML
    private TextField textFieldNewStudentLastName;
    @FXML
    private TextField textFieldNewStudentEmail;
    @FXML
    private TextField textFieldNewProfessorName;
    @FXML
    private TextField textFieldNewProfessorLastName;
    @FXML
    private ComboBox<String> comboBoxNewProfessorDegree;
    @FXML
    private TextField textFieldNewProfessorEmail;
    @FXML
    private TableView<AccessAccount> tableViewAccessAccounts;
    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr." ,"Dra.", "MCC.");

    private void fillTableViewAccessAccounts() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        try {
            tableViewAccessAccounts.getItems().addAll(accessAccountDAO.getAccessAccountsList());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuparar la informaicón de la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void initialize() {
        TableColumn<AccessAccount, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idColumn.setMinWidth(30);
        idColumn.setMaxWidth(30);
        TableColumn<AccessAccount, String> usernameColumn = new TableColumn<>("Usuario");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<AccessAccount, String> userTypeColumn = new TableColumn<>("Tipo de usuario");
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        tableViewAccessAccounts.getColumns().addAll(idColumn, usernameColumn, userTypeColumn);
        fillTableViewAccessAccounts();
        comboBoxDegree.setItems(observableListComboItemsDegree);
        comboBoxUserType.setItems(observableListComboItemsUserType);
        comboBoxUserTypeToModify.setItems(observableListComboItemsUserType);
    }

    @FXML
    private void buttonSaveAction() {
        if (areAddUserFieldsValid()) {
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUsername(textFieldUsername.getText());
            accessAccount.setUserPassword(passwordFieldPassword.getText());
            accessAccount.setUserType(comboBoxUserType.getValue());
            if (comboBoxUserType.getValue().equals("Estudiante")) {
                Student student = new Student();
                student.setStudentID(textFieldStudentId.getText());
                student.setName(textFieldStudentName.getText());
                student.setLastName(textFieldStudentLastName.getText());
                student.setAcademicEmail(textFieldStudentEmail.getText());
                try {
                    accessAccountDAO.transactionAddStudentUser(accessAccount, student);
                    DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
                }
            } else if (comboBoxUserType.getValue().equals("Profesor") || comboBoxUserType.getValue().equals("RepresentanteCA")) {
                Professor professor = new Professor();
                professor.setProfessorName(textFieldProfessorName.getText());
                professor.setProfessorLastName(textFieldProfessorLastName.getText());
                professor.setProfessorDegree(comboBoxDegree.getValue());
                professor.setProfessorEmail(textFieldProfessorEmail.getText());
                try {
                    accessAccountDAO.transactionAddProfessorUser(accessAccount, professor);
                    DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
                }
            } else if (comboBoxUserType.getValue().equals("Administrador")) {
                try {
                    addAdminAccessAccount();
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("No se pudo acceder añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
                }
            }
            tableViewAccessAccounts.getItems().clear();
            fillTableViewAccessAccounts();
        }
    }

    @FXML
    private void buttonModifyAction() {
        if (tableViewAccessAccounts.getSelectionModel().getSelectedItem() != null) {
            textFieldUserToModify.setText(tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername());
            String userType = tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUserType();
            comboBoxUserTypeToModify.getSelectionModel().select(userType);
            tabPaneAccountManagement.getSelectionModel().select(2);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar a un usuario para modificarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void buttonConfirmModificationAction() throws SQLException {
        if (areModifyUserFieldsValid()) {
            AccessAccount accessAccount = new AccessAccount();
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            String usernameToModify = textFieldUserToModify.getText();
            accessAccount.setUsername(textFieldNewUsername.getText());
            accessAccount.setUserPassword(textFieldNewPassword.getText());
            accessAccount.setUserType(comboBoxUserTypeToModify.getValue());
            if (gridPaneNewStudent.isVisible()) {
                Student student = new Student();
                student.setStudentID(textFieldNewStudentId.getText());
                student.setName(textFieldNewStudentName.getText());
                student.setLastName(textFieldNewStudentLastName.getText());
                student.setAcademicEmail(textFieldNewStudentEmail.getText());
                accessAccountDAO.modifyStudentUserTransaction(usernameToModify, accessAccount, student);
            } else if (gridPaneNewProfessor.isVisible()) {
                Professor professor = new Professor();
                professor.setProfessorName(textFieldNewProfessorName.getText());
                professor.setProfessorLastName(textFieldNewProfessorLastName.getText());
                professor.setProfessorDegree(comboBoxNewProfessorDegree.getValue());
                professor.setProfessorEmail(textFieldNewProfessorEmail.getText());
                accessAccountDAO.modifyProfessorUserTransaction(usernameToModify, accessAccount, professor);
            } else {
                addAdminAccessAccount();
                tableViewAccessAccounts.getItems().add(accessAccount);
            }
        }
    }

    @FXML
    private void buttonDeleteAction() throws SQLException {
        if (tableViewAccessAccounts.getSelectionModel().getSelectedItem() == null) {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar al usuario que quieres eliminar", AlertStatus.WARNING));
        } else {
            String username = tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername();
            if (isUserAdmin(username)) {
                DialogGenerator.getDialog(new AlertMessage("No se pueden eliminar los usuarios administrador", AlertStatus.WARNING));
            } else {
                deleteUser(username);
                tableViewAccessAccounts.getItems().clear();
                fillTableViewAccessAccounts();
            }
        }
    }

    @FXML
    private void handleAddUserTypeSelection() {
        switch (comboBoxUserType.getValue()) {
            case "Profesor", "RepresentanteCA" -> {
                gridPaneStudent.setVisible(false);
                gridPaneProfessor.setVisible(true);
            }
            case "Estudiante" -> {
                gridPaneStudent.setVisible(true);
                gridPaneProfessor.setVisible(false);
            }
            default -> {
                gridPaneProfessor.setVisible(false);
                gridPaneStudent.setVisible(false);
            }
        }
    }

    @FXML
    private void handleModifyUserTypeSelection() {
        switch (comboBoxUserTypeToModify.getValue()) {
            case "Profesor", "RepresentanteCA" -> {
                gridPaneNewProfessor.setVisible(true);
                gridPaneNewStudent.setVisible(false);
            }
            case "Estudiante" -> {
                gridPaneNewProfessor.setVisible(false);
                gridPaneNewStudent.setVisible(true);
            }
            default -> {
                gridPaneNewProfessor.setVisible(false);
                gridPaneNewStudent.setVisible(false);
            }
        }
    }

    @FXML
    private void actionLogOut() throws IOException {
        logOut();
    }

    private static final int MAX_LENGTH_USERNAME = 28;
    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_STUDENT_ID = 10;
    private boolean areAddUserFieldsValid() {
        if (gridPaneProfessor.isVisible()) {
            if (textFieldUsername.getText().isBlank()
                    || passwordFieldPassword.getText().isBlank()
                    || comboBoxUserType.getValue().isBlank()
                    || textFieldProfessorName.getText().isBlank()
                    || textFieldProfessorLastName.getText().isBlank()
                    || comboBoxDegree.getValue().isBlank()
                    || textFieldProfessorEmail.getText().isBlank()) {
                DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
                return false;
            } else return textFieldUsername.getText().length() < MAX_LENGTH_USERNAME
                    && passwordFieldPassword.getText().length() < MAX_LENGTH_PASSWORD
                    && textFieldProfessorName.getText().length() < MAX_LENGTH_NAME
                    && textFieldProfessorLastName.getText().length() < MAX_LENGTH_LASTNAME
                    && textFieldProfessorEmail.getText().length() < MAX_LENGTH_EMAIL;
        } else if (gridPaneStudent.isVisible()) {
            if (textFieldUsername.getText().isBlank()
                    || passwordFieldPassword.getText().isBlank()
                    || comboBoxUserType.getValue().isBlank()
                    || textFieldStudentId.getText().isBlank()
                    || textFieldStudentName.getText().isBlank()
                    || textFieldStudentLastName.getText().isBlank()
                    || textFieldStudentEmail.getText().isBlank()) {
                DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
                return false;
            } else return textFieldUsername.getText().length() < MAX_LENGTH_USERNAME
                    && passwordFieldPassword.getText().length() < MAX_LENGTH_PASSWORD
                    && textFieldStudentId.getText().length() < MAX_LENGTH_STUDENT_ID
                    && textFieldStudentName.getText().length() < MAX_LENGTH_NAME
                    && textFieldStudentLastName.getText().length() < MAX_LENGTH_LASTNAME
                    && textFieldStudentEmail.getText().length() < MAX_LENGTH_EMAIL;
        } else {
            return false;
        }
    }

    private boolean areModifyUserFieldsValid() {
        if (gridPaneNewProfessor.isVisible()) {
            if (textFieldUserToModify.getText().isBlank()
                    || textFieldNewPassword.getText().isBlank()
                    || comboBoxUserTypeToModify.getValue().isBlank()
                    || textFieldNewProfessorName.getText().isBlank()
                    || textFieldNewProfessorLastName.getText().isBlank()
                    || comboBoxNewProfessorDegree.getValue().isBlank()
                    || textFieldNewProfessorEmail.getText().isBlank()) {
                DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
                return false;
            } else return textFieldUserToModify.getText().length() < MAX_LENGTH_USERNAME
                    && textFieldNewPassword.getText().length() < MAX_LENGTH_PASSWORD
                    && textFieldNewProfessorName.getText().length() < MAX_LENGTH_NAME
                    && textFieldNewProfessorLastName.getText().length() < MAX_LENGTH_LASTNAME
                    && textFieldNewProfessorEmail.getText().length() < MAX_LENGTH_EMAIL;
        } else if (gridPaneNewStudent.isVisible()) {
            if (textFieldUserToModify.getText().isBlank()
                    || textFieldNewPassword.getText().isBlank()
                    || comboBoxUserTypeToModify.getValue().isBlank()
                    || textFieldNewStudentId.getText().isBlank()
                    || textFieldNewStudentName.getText().isBlank()
                    || textFieldNewStudentLastName.getText().isBlank()
                    || textFieldNewStudentEmail.getText().isBlank()) {
                DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
                return false;
            } else return textFieldUserToModify.getText().length() < MAX_LENGTH_USERNAME
                    && textFieldNewPassword.getText().length() < MAX_LENGTH_PASSWORD
                    && textFieldNewStudentId.getText().length() < MAX_LENGTH_STUDENT_ID
                    && textFieldNewStudentName.getText().length() < MAX_LENGTH_NAME
                    && textFieldNewStudentLastName.getText().length() < MAX_LENGTH_LASTNAME
                    && textFieldNewStudentEmail.getText().length() < MAX_LENGTH_EMAIL;
        } else {
            return true;
        }
    }

    private boolean isUserAdmin(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals("Administrador");
    }

    private void addAdminAccessAccount() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccountDAO.addAdminAccessAccount(accessAccount);
    }
    
    public boolean confirmedDeleteUser(String displayUsername) {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar al usuario " + displayUsername + "?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void deleteUser(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        if(confirmedDeleteUser(username)) {
            accessAccountDAO.deleteUserByUsername(username);
        }
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void logOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
