package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.dao.ProfessorDAO;
import mx.uv.fei.dao.StudentDAO;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;

import java.io.IOException;
import java.sql.SQLException;
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
    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsFilter =
            FXCollections.observableArrayList("Todos" ,"Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr." ,"Dra.", "MCC.");
    private static final int MAX_FIELD_LENGTH = 28;
    private String userToRegister;

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
            if (gridPaneStudent.isVisible()) {
                addAccessAccount();
                addStudent();
                //try and alert
            } else if (gridPaneProfessor.isVisible()) {
                addAccessAccount();
                addProfessor();
                //try and alert
            } else {
                addAccessAccount();
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
        if (areModifyUserFieldsValid()) {
            modifyAccessAccountAttributesByUsername(textFieldUserToModify.getText(), textFieldNewPassword.getText(), comboBoxUserTypeModify.getValue());
        } else {
            //somealert
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
                confirmDeletion();
                updateListView();
            }
        }
    }
    @FXML
    private void handleUserType() {
        switch (comboBoxUserType.getValue()) {
            case "Profesor", "RepresentanteCA" -> {
                gridPaneStudent.setVisible(false);
                gridPaneProfessor.setVisible(true);
                userToRegister = "Profesor";
            }
            case "Estudiante" -> {
                gridPaneStudent.setVisible(true);
                gridPaneProfessor.setVisible(false);
                userToRegister = "Estudiante";
            }
            default -> {
                gridPaneProfessor.setVisible(false);
                gridPaneStudent.setVisible(false);
                userToRegister = "Administrador";
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

    private boolean areAddUserFieldsValid() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (textFieldUsername.getText().isBlank() || passwordFieldPassword.getText().isBlank()
                || comboBoxUserType.getValue().isBlank()) {
            alert.setTitle("Error en los campos");
            alert.show();
            return false;
        } else {
            if (textFieldUsername.getText().length() > MAX_FIELD_LENGTH
                    || passwordFieldPassword.getText().length() > MAX_FIELD_LENGTH) {
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

    private void addAccessAccount() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccountDAO.addAccessAccount(accessAccount);
    }

    private void addStudent() { // can be moved to logic
        StudentDAO studentDAO = new StudentDAO();
        Student student = new Student();
        student.setStudentID(textFieldStudentId.getText());
        student.setName(textFieldStudentName.getText());
        student.setLastName(textFieldStudentLastName.getText());
        student.setAcademicEmail(textFieldStudentEmail.getText());
        student.setUsername(textFieldUsername.getText());
        try {
            studentDAO.insertStudent(student);
        } catch (SQLException sqlException) {
            //some
        }
    }

    private void addProfessor() { // can be moved to logic
        Professor professor = new Professor();
        ProfessorDAO professorDAO = new ProfessorDAO();
        professor.setProfessorName(textFieldProfessorName.getText());
        professor.setProfessorLastName(textFieldProfessorLastName.getText());
        professor.setProfessorEmail(textFieldProfessorEmail.getText());
        professor.setProfessorDegree(comboBoxDegree.getValue());
        professor.setUsername(textFieldUsername.getText());
        try {
            professorDAO.addProfessor(professor);
        } catch (SQLException sqlException) {
            //some alert
        }
    }

    private void modifyAccessAccountAttributesByUsername(String username, String newPassword, String userType) throws SQLException {
        // can be moved to logic
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        accessAccountDAO.modifyAccessAccountByUsername(username, newPassword, userType);
    }

    private void confirmDeletion() throws SQLException { // can be moved to logic somewhat
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
        LoginController.sessionDetails.cleanSessionDetails();
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
