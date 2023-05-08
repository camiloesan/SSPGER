package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.AccessAccountDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;

import static mx.uv.fei.gui.MainStage.HEIGHT_OFFSET;

public class PaneModifyUserController {
    @FXML
    private Label labelHeader;
    @FXML
    private ComboBox<String> comboBoxNewProfessorDegree;
    @FXML
    private ComboBox<String> comboBoxUserTypeToModify;
    @FXML
    private GridPane gridPaneNewProfessor;
    @FXML
    private GridPane gridPaneNewStudent;
    @FXML
    private PasswordField textFieldNewPassword;
    @FXML
    private TextField textFieldNewProfessorEmail;
    @FXML
    private TextField textFieldNewProfessorLastName;
    @FXML
    private TextField textFieldNewProfessorName;
    @FXML
    private TextField textFieldNewStudentEmail;
    @FXML
    private TextField textFieldNewStudentId;
    @FXML
    private TextField textFieldNewStudentLastName;
    @FXML
    private TextField textFieldNewStudentName;
    @FXML
    private TextField textFieldNewUsername;

    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList("Estudiante", "Profesor", "RepresentanteCA");

    @FXML
    private void initialize() {
        labelHeader.setText("Modificar usuario [" + AccessAccountManagementController.getUsername() + "]");
        comboBoxUserTypeToModify.setItems(observableListComboItemsUserType);
    }

    @FXML
    private void buttonConfirmModificationAction() {
        if (areAccessAccountFieldsValid()) {
            switch (comboBoxUserTypeToModify.getValue()) {
                case "Profesor", "RepresentanteCA" -> {
                    if (areProfessorFieldsValid()) {
                        modifyProfessorUser();
                    }
                }
                case "Estudiante" -> {
                    if (areStudentFieldsValid()) {
                        modifyStudentUser();
                    }
                }
                default -> DialogGenerator.getDialog(new AlertMessage("Debes seleccionar un tipo de usuario", AlertStatus.WARNING));
            }
        }
    }

    private boolean areAccessAccountFieldsValid() {
        if (textFieldNewPassword.getText().isBlank()
                || comboBoxUserTypeToModify.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldNewUsername.getText().length() > MAX_LENGTH_USERNAME
                || textFieldNewPassword.getText().length() > MAX_LENGTH_PASSWORD){
            DialogGenerator.getDialog(new AlertMessage("Has sobrepasado el límite de caracteres, inténtalo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private static final int MAX_LENGTH_USERNAME = 28;
    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_STUDENT_ID = 10;

    private boolean areProfessorFieldsValid() {
        if (textFieldNewProfessorName.getText().isBlank()
                || textFieldNewProfessorLastName.getText().isBlank()
                || comboBoxNewProfessorDegree.getValue() == null
                || textFieldNewProfessorEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if(textFieldNewProfessorName.getText().length() > MAX_LENGTH_NAME
                || textFieldNewProfessorLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldNewProfessorEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean areStudentFieldsValid() {
        if (textFieldNewStudentId.getText().isBlank()
                || textFieldNewStudentName.getText().isBlank()
                || textFieldNewStudentLastName.getText().isBlank()
                || textFieldNewStudentEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldNewStudentId.getText().length() > MAX_LENGTH_STUDENT_ID
                || textFieldNewStudentName.getText().length() > MAX_LENGTH_NAME
                || textFieldNewStudentLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldNewStudentEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private void modifyProfessorUser() {
        AccessAccount accessAccount = new AccessAccount();
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        accessAccount.setUsername(textFieldNewUsername.getText());
        accessAccount.setUserPassword(textFieldNewPassword.getText());
        accessAccount.setUserType(comboBoxUserTypeToModify.getValue());
        Professor professor = new Professor();
        professor.setProfessorName(textFieldNewProfessorName.getText());
        professor.setProfessorLastName(textFieldNewProfessorLastName.getText());
        professor.setProfessorDegree(comboBoxNewProfessorDegree.getValue());
        professor.setProfessorEmail(textFieldNewProfessorEmail.getText());
        try {
            accessAccountDAO.modifyProfessorUserTransaction(AccessAccountManagementController.getUsername(), accessAccount, professor);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo modificar al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    private void modifyStudentUser() {
        AccessAccount accessAccount = new AccessAccount();
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        accessAccount.setUsername(textFieldNewUsername.getText());
        accessAccount.setUserPassword(textFieldNewPassword.getText());
        accessAccount.setUserType(comboBoxUserTypeToModify.getValue());
        Student student = new Student();
        student.setStudentID(textFieldNewStudentId.getText());
        student.setName(textFieldNewStudentName.getText());
        student.setLastName(textFieldNewStudentLastName.getText());
        student.setAcademicEmail(textFieldNewStudentEmail.getText());
        try {
            accessAccountDAO.modifyStudentUserTransaction(AccessAccountManagementController.getUsername(), accessAccount, student);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo modificar al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
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
    private void returnToUsersView() throws IOException {
        MainStage.changeView("accessaccountmanagement-view.fxml", 1000, 600 + HEIGHT_OFFSET);
    }
}
