package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;

import java.io.IOException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccessAccountManagementController {
    @FXML
    private ListView<String> listViewUsernames;
    @FXML
    private ComboBox<String> comboBoxUserType;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private TextField textFieldUsername;
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
    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsFilter =
            FXCollections.observableArrayList("Todos" ,"Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr." ,"Dra.", "MCC.");

    @FXML
    private void initialize() throws SQLException {
        updateListView();
        comboBoxDegree.setItems(observableListComboItemsDegree);
        comboBoxUserType.setItems(observableListComboItemsUserType);
        comboBoxFilter.setItems(observableListComboItemsFilter);
        comboBoxUserTypeModify.setItems(observableListComboItemsUserType);
    }

    @FXML
    private void buttonSaveAction() throws SQLException {
        if (areAddUserFieldsValid()) {
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUsername(textFieldUsername.getText());
            accessAccount.setUserPassword(passwordFieldPassword.getText());
            accessAccount.setUserType(comboBoxUserType.getValue());
            if (gridPaneStudent.isVisible()) {
                Student student = new Student();
                student.setStudentID(textFieldNewStudentId.getText());
                student.setName(textFieldNewStudentName.getText());
                student.setLastName(textFieldNewStudentLastName.getText());
                student.setAcademicEmail(textFieldNewStudentEmail.getText());
                accessAccountDAO.addStudentUserTransaction(accessAccount, student);
                //try and alert
            } else if (gridPaneProfessor.isVisible()) {
                Professor professor = new Professor();
                professor.setProfessorName(textFieldProfessorName.getText());
                professor.setProfessorLastName(textFieldProfessorLastName.getText());
                professor.setProfessorDegree(comboBoxDegree.getValue());
                professor.setProfessorEmail(textFieldProfessorEmail.getText());
                accessAccountDAO.addProfessorUserTransaction(accessAccount, professor);
                //try and alert
            }
            updateListView();
        }
    }

    @FXML
    private void buttonModifyAction() {
        textFieldUserToModify.setText(listViewUsernames.getSelectionModel().getSelectedItem());
        tabPaneAccountManagement.getSelectionModel().select(2);
    }

    @FXML
    private void buttonConfirmModificationAction() throws SQLException {
        if (!areModifyUserFieldsValid()) {
            //some alert
        } else {
            AccessAccount accessAccount = new AccessAccount();
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            String usernameToModify = textFieldUserToModify.getText();
            accessAccount.setUsername(textFieldNewUsername.getText());
            accessAccount.setUserPassword(textFieldNewPassword.getText());
            accessAccount.setUserType(comboBoxUserTypeModify.getValue());
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
            }
        }
    }

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
                deleteUser(username);
                updateListView();
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
        switch (comboBoxUserTypeModify.getValue()) {
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

    @FXML
    private void updateListView() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        listViewUsernames.setItems(FXCollections.observableList(accessAccountDAO.getListAccessAccounts()));
    }

    @FXML
    private void handleUserTypeFilter() throws SQLException {
        if (comboBoxFilter.getValue().equals("Todos")) {
            updateListView();
        } else {
            AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
            listViewUsernames.setItems(FXCollections.observableList(accessAccountDAO.getUsernamesByUsertype(comboBoxFilter.getValue())));
        }
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
                //alert todos los campos deben estar llenos
                return false;
            } else if (textFieldUsername.getText().length() >= MAX_LENGTH_USERNAME
                    || passwordFieldPassword.getText().length() >= MAX_LENGTH_PASSWORD
                    || textFieldProfessorName.getText().length() >= MAX_LENGTH_NAME
                    || textFieldProfessorLastName.getText().length() >= MAX_LENGTH_LASTNAME
                    || textFieldProfessorEmail.getText().length() >= MAX_LENGTH_EMAIL) {
                //alert has sobrepasado el límite de caracteres, revisa los campos de nuevo
                return false;
            } else {
                return true;
            }
        } else if (gridPaneStudent.isVisible()) {
            if (textFieldUsername.getText().isBlank()
                    || passwordFieldPassword.getText().isBlank()
                    || comboBoxUserType.getValue().isBlank()
                    || textFieldStudentId.getText().isBlank()
                    || textFieldStudentName.getText().isBlank()
                    || textFieldStudentLastName.getText().isBlank()
                    || textFieldStudentEmail.getText().isBlank()) {
                //alert todos los campos deben estar llenos
                return false;
            } else if (textFieldUsername.getText().length() >= MAX_LENGTH_USERNAME
                    || passwordFieldPassword.getText().length() >= MAX_LENGTH_PASSWORD
                    || textFieldStudentId.getText().length() >= MAX_LENGTH_STUDENT_ID
                    || textFieldStudentName.getText().length() >= MAX_LENGTH_NAME
                    || textFieldStudentLastName.getText().length() >= MAX_LENGTH_LASTNAME
                    || textFieldStudentEmail.getText().length() >= MAX_LENGTH_EMAIL) {
                //alert has sobrepasado el límite de caracteres
                return false;
            } else {
                return true;
            }
        } else {
            //alert debes seleccionar un tipo de usuario
            return false;
        }
    }

    private boolean areModifyUserFieldsValid() {
        if (gridPaneNewProfessor.isVisible()) {
            if (textFieldUserToModify.getText().isBlank()
                    || textFieldNewPassword.getText().isBlank()
                    || comboBoxUserTypeModify.getValue().isBlank()
                    || textFieldNewProfessorName.getText().isBlank()
                    || textFieldNewProfessorLastName.getText().isBlank()
                    || comboBoxNewProfessorDegree.getValue().isBlank()
                    || textFieldNewProfessorEmail.getText().isBlank()) {
                //alert campos vacios
                return false;
            } else if (textFieldUserToModify.getText().length() >= MAX_LENGTH_USERNAME
                    || textFieldNewPassword.getText().length() >= MAX_LENGTH_PASSWORD
                    || textFieldNewProfessorName.getText().length() >= MAX_LENGTH_NAME
                    || textFieldNewProfessorLastName.getText().length() >= MAX_LENGTH_NAME
                    || textFieldNewProfessorEmail.getText().length() >= MAX_LENGTH_EMAIL) {
                //alert se sobrepaso el límite de caracteres
                return false;
            } else {
                return true;
            }
        } else if (gridPaneNewStudent.isVisible()) {
            if (textFieldUserToModify.getText().isBlank()
                    || textFieldNewPassword.getText().isBlank()
                    || comboBoxUserTypeModify.getValue().isBlank()
                    || textFieldNewProfessorName.getText().isBlank()
                    || textFieldNewProfessorLastName.getText().isBlank()
                    || comboBoxNewProfessorDegree.getValue().isBlank()
                    || textFieldNewProfessorEmail.getText().isBlank()) {
                //alert campos vacios
                return false;
            } else if (textFieldUserToModify.getText().length() >= MAX_LENGTH_USERNAME
                    || textFieldNewPassword.getText().length() >= MAX_LENGTH_PASSWORD
                    || textFieldNewProfessorName.getText().length() >= MAX_LENGTH_NAME
                    || textFieldNewProfessorLastName.getText().length() >= MAX_LENGTH_LASTNAME
                    || textFieldNewProfessorEmail.getText().length() >= MAX_LENGTH_EMAIL) {
                //alert limite sobrepasado
                return false;
            } else {
                return true;
            }
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
            accessAccountDAO.deleteAccessAccountByUsername(username);
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
