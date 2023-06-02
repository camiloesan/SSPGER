package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PaneAddUserController {
    @FXML
    private ComboBox<String> comboBoxDegree;
    @FXML
    private ComboBox<String> comboBoxUserType;
    @FXML
    private GridPane gridPaneProfessor;
    @FXML
    private GridPane gridPaneStudent;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private TextField textFieldProfessorEmail;
    @FXML
    private TextField textFieldProfessorLastName;
    @FXML
    private TextField textFieldProfessorName;
    @FXML
    private TextField textFieldStudentEmail;
    @FXML
    private TextField textFieldStudentId;
    @FXML
    private TextField textFieldStudentLastName;
    @FXML
    private TextField textFieldStudentName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private Button buttonSave;
    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList(LoginController.USER_ADMIN,
                    LoginController.USER_STUDENT,
                    LoginController.USER_PROFESSOR,
                    LoginController.USER_REPRESENTATIVE);
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr.", "Dra.", "MCC.");
    private static final Logger logger = Logger.getLogger(PaneAddUserController.class);


    @FXML
    private void initialize() {
        comboBoxDegree.setItems(observableListComboItemsDegree);
        comboBoxUserType.setItems(observableListComboItemsUserType);
    }

    @FXML
    void buttonSaveAction() {
        if (areAccessAccountFieldsValid()) {
            switch (comboBoxUserType.getValue()) {
                case LoginController.USER_PROFESSOR, LoginController.USER_REPRESENTATIVE -> {
                    if (areProfessorFieldsValid()) {
                        addProfessorUser();
                    }
                }
                case LoginController.USER_STUDENT -> {
                    if (areStudentFieldsValid()) {
                        addStudentUser();
                    }
                }
                case LoginController.USER_ADMIN -> addAdminUser();
                default -> DialogGenerator.getDialog(new AlertMessage("Debes seleccionar un tipo de usuario", AlertStatus.WARNING));
            }
            clearFields();
        }
    }

    private void clearFields() {
        comboBoxDegree.setValue(null);
        passwordFieldPassword.setText("");
        textFieldProfessorEmail.setText("");
        textFieldProfessorName.setText("");
        textFieldProfessorLastName.setText("");
        textFieldStudentEmail.setText("");
        textFieldStudentId.setText("");
        textFieldStudentName.setText("");
        textFieldStudentLastName.setText("");
        textFieldUsername.setText("");
    }

    private void addAdminUser() {
        UserDAO accessAccountDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        try {
            accessAccountDAO.addAdminUser(accessAccount);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo agregar al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    private void addProfessorUser() {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        Professor professor = new Professor();
        professor.setProfessorName(textFieldProfessorName.getText());
        professor.setProfessorLastName(textFieldProfessorLastName.getText());
        professor.setProfessorDegree(comboBoxDegree.getValue());
        professor.setProfessorEmail(textFieldProfessorEmail.getText());
        try {
            userDAO.addProfessorUserTransaction(accessAccount, professor);
            DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    private void addStudentUser() {
        UserDAO accessAccountDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        Student student = new Student();
        student.setStudentID(textFieldStudentId.getText());
        student.setName(textFieldStudentName.getText());
        student.setLastName(textFieldStudentLastName.getText());
        student.setAcademicEmail(textFieldStudentEmail.getText());
        try {
            accessAccountDAO.addStudentUserTransaction(accessAccount, student);
            DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo más tarde", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    private static final int MAX_LENGTH_USERNAME = 28;
    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_STUDENT_ID = 10;

    private boolean areAccessAccountFieldsValid() {
        if (textFieldUsername.getText().isBlank()
                || passwordFieldPassword.getText().isBlank()
                || comboBoxUserType.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldUsername.getText().length() > MAX_LENGTH_USERNAME
                || passwordFieldPassword.getText().length() > MAX_LENGTH_PASSWORD){
            DialogGenerator.getDialog(new AlertMessage("Has sobrepasado el límite de caracteres, inténtalo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean areProfessorFieldsValid() {
        if (textFieldProfessorName.getText().isBlank()
                || textFieldProfessorLastName.getText().isBlank()
                || comboBoxDegree.getValue() == null
                || textFieldProfessorEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if(textFieldProfessorName.getText().length() > MAX_LENGTH_NAME
                || textFieldProfessorLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldProfessorEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean areStudentFieldsValid() {
        if (textFieldStudentId.getText().isBlank()
                || textFieldStudentName.getText().isBlank()
                || textFieldStudentLastName.getText().isBlank()
                || textFieldStudentEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldStudentId.getText().length() > MAX_LENGTH_STUDENT_ID
                || textFieldStudentName.getText().length() > MAX_LENGTH_NAME
                || textFieldStudentLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldStudentEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void handleAddUserTypeSelection() {
        buttonSave.setVisible(true);
        switch (comboBoxUserType.getValue()) {
            case LoginController.USER_PROFESSOR, LoginController.USER_REPRESENTATIVE -> {
                gridPaneStudent.setVisible(false);
                gridPaneProfessor.setVisible(true);
            }
            case LoginController.USER_STUDENT -> {
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
    private void returnToMainMenu() throws IOException {
        MainStage.changeView("usermanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    @FXML
    private void logOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
