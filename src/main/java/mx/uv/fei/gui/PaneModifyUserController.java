package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static mx.uv.fei.gui.MainStage.HEIGHT_OFFSET;

public class PaneModifyUserController {
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
    private TextField textFieldNewEmail;
    @FXML
    private TextField textFieldNewProfessorLastName;
    @FXML
    private TextField textFieldNewProfessorName;
    @FXML
    private TextField textFieldNewStudentId;
    @FXML
    private TextField textFieldNewStudentLastName;
    @FXML
    private TextField textFieldNewStudentName;
    @FXML
    private TextField textFieldUserToModify;
    private static final Logger logger = Logger.getLogger(PaneModifyUserController.class);

    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList(LoginController.USER_STUDENT,
                    LoginController.USER_PROFESSOR,
                    LoginController.USER_REPRESENTATIVE);
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr." ,"Dra.", "MCC.");

    @FXML
    private void initialize() {
        comboBoxUserTypeToModify.setValue(UserManagementController.getUserType());
        switch (UserManagementController.getUserType()) {
            case LoginController.USER_STUDENT -> {
                gridPaneNewStudent.setVisible(true);
                showStudentData();
            }
            case LoginController.USER_PROFESSOR, LoginController.USER_REPRESENTATIVE -> {
                gridPaneNewProfessor.setVisible(true);
                showProfessorData();
            }
        }
        textFieldUserToModify.setText(UserManagementController.getUsername());
        comboBoxUserTypeToModify.setItems(observableListComboItemsUserType);
        comboBoxNewProfessorDegree.setItems(observableListComboItemsDegree);
    }
    
    private void showProfessorData() {
        UserDAO userDAO = new UserDAO();
        Professor professorData;
        try {
            professorData = userDAO.getProfessorAccount(UserManagementController.getUsername());
            textFieldNewEmail.setText(professorData.getEmail());
            textFieldNewProfessorName.setText(professorData.getProfessorName());
            textFieldNewProfessorLastName.setText(professorData.getProfessorLastName());
            comboBoxNewProfessorDegree.setValue(professorData.getProfessorDegree());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información de la base de datos", AlertStatus.WARNING));
            logger.error(sqlException);
        }
    }
    
    private void showStudentData() {
        UserDAO userDAO = new UserDAO();
        Student studentData;
        try {
            studentData = userDAO.getStudentAccount(UserManagementController.getUsername());
            textFieldNewEmail.setText(studentData.getEmail());
            textFieldNewStudentId.setText(studentData.getStudentID());
            textFieldNewStudentName.setText(studentData.getName());
            textFieldNewStudentLastName.setText(studentData.getLastName());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información de la base de datos", AlertStatus.WARNING));
            logger.error(sqlException);
        }
    }

    private static final String PROFESSOR_USER = "Profesor";
    private static final String STUDENT__USER = "Estudiante";
    private static final String ACADEMIC_BODY_REPRESENTATIVE_USER = "RepresentanteCA";

    @FXML
    private void buttonConfirmModificationAction() {
        String userType = comboBoxUserTypeToModify.getValue();
        if (areFieldsValid(userType)) {
            switch (userType) {
                case PROFESSOR_USER, ACADEMIC_BODY_REPRESENTATIVE_USER -> modifyProfessorUser();
                case STUDENT__USER -> modifyStudentUser();
            }
        }
    }

    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_STUDENT_ID = 10;

    private boolean areFieldsValid(String userType) {
        UserDAO userDAO = new UserDAO();
        if (textFieldNewPassword.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldNewPassword.getText().length() > MAX_LENGTH_PASSWORD) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El campo contraseña debe tener máximo 64 caracteres", AlertStatus.WARNING
            ));
        } else if (textFieldNewEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El campo correo electrónico debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
        } else {
            try {
                if (userDAO.isEmailTaken(textFieldNewEmail.getText())) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "El correo electrónico ya está registrado, intente con uno distinto", AlertStatus.WARNING
                    ));
                } else {
                    switch (userType) {
                        case PROFESSOR_USER, ACADEMIC_BODY_REPRESENTATIVE_USER -> {
                            return areProfessorFieldsValid();
                        }
                        case STUDENT__USER -> {
                            return areStudentFieldsValid();
                        }
                    }
                }
            } catch (SQLException e) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Ocurrió un error con la base de datos, inténtelo más tarde", AlertStatus.WARNING
                ));
            }
        }
        return false;
    }

    private boolean areProfessorFieldsValid() {
        Professor professor = new Professor();
        if (textFieldNewProfessorName.getText().isBlank()
                || textFieldNewProfessorLastName.getText().isBlank()
                || comboBoxNewProfessorDegree.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldNewProfessorName.getText().length() > MAX_LENGTH_NAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el campo nombre del profesor debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldNewProfessorLastName.getText().length() > MAX_LENGTH_LASTNAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el campo apellidos debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (!professor.isEmailValid(textFieldNewEmail.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El formato del correo electrónico no es válido, " +
                            "sólo se permiten caracteres alfa-numéricos y direcciones válidas de personal (@uv.mx)",
                    AlertStatus.WARNING
            ));
            return false;
        } else {
            return true;
        }
    }

    private boolean areStudentFieldsValid() {
        Student student = new Student();
        if (textFieldNewStudentId.getText().isBlank()
                || textFieldNewStudentName.getText().isBlank()
                || textFieldNewStudentLastName.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldNewStudentId.getText().length() != MAX_LENGTH_STUDENT_ID) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, la matrícula debe tener exactamente 10 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldNewStudentName.getText().length() > MAX_LENGTH_NAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el límite del nombre es de 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldNewStudentLastName.getText().length() > MAX_LENGTH_LASTNAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el límite del campo apellidos es de máximo 80 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldNewEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el correo electrónico debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (!student.isEmailValid(textFieldNewEmail.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El formato del correo electrónico no es válido, " +
                            "sólo se permiten caracteres alfa-numéricos y direcciones válidas de estudiante (@estudiantes.uv.mx)",
                    AlertStatus.WARNING
            ));
            return false;
        } else {
            return true;
        }
    }

    private void modifyProfessorUser() {
        AccessAccount accessAccount = new AccessAccount();
        UserDAO userDAO = new UserDAO();
        accessAccount.setUserPassword(textFieldNewPassword.getText());
        accessAccount.setUserType(comboBoxUserTypeToModify.getValue());
        accessAccount.setUserEmail(textFieldNewEmail.getText());
        Professor professor = new Professor();
        professor.setProfessorName(textFieldNewProfessorName.getText());
        professor.setProfessorLastName(textFieldNewProfessorLastName.getText());
        professor.setProfessorDegree(comboBoxNewProfessorDegree.getValue());

        boolean modifiedSuccessfully = false;
        try {
            modifiedSuccessfully = userDAO
                    .modifyProfessorUserTransaction(UserManagementController.getUsername(), accessAccount, professor);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo modificar al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            logger.error(sqlException);
        }

        if (modifiedSuccessfully) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Se ha modificado con éxito el usuario", AlertStatus.SUCCESS
            ));
        }
    }

    private void modifyStudentUser() {
        AccessAccount accessAccount = new AccessAccount();
        UserDAO userDAO = new UserDAO();
        StudentDAO studentDAO = new StudentDAO();
        accessAccount.setUserPassword(textFieldNewPassword.getText());
        accessAccount.setUserType(comboBoxUserTypeToModify.getValue());
        accessAccount.setUserEmail(textFieldNewEmail.getText());
        Student student = new Student();
        student.setStudentID(textFieldNewStudentId.getText());
        student.setName(textFieldNewStudentName.getText());
        student.setLastName(textFieldNewStudentLastName.getText());

        boolean isStudentIDTaken = true;
        try {
            isStudentIDTaken = studentDAO.isStudentIDTaken(student.getStudentID());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Ocurrió un error con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR
            ));
            logger.error(sqlException);
        }

        boolean modifiedSuccessfully = false;
        if (isStudentIDTaken) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Esa matrícula ya está registrada, inténtalo de nuevo", AlertStatus.WARNING
            ));
        } else {
            try {
                modifiedSuccessfully = userDAO.
                        modifyStudentUserTransaction(UserManagementController.getUsername(), accessAccount, student);
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error con la base de datos, no se pudo modificar al usuario, inténtelo de nuevo más tarde",
                        AlertStatus.ERROR));
                logger.error(sqlException);
            }
        }

        if (modifiedSuccessfully) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Se ha modificado con éxito el usuario", AlertStatus.SUCCESS
            ));
        }
    }

    @FXML
    private void handleModifyUserTypeSelection() {
        switch (comboBoxUserTypeToModify.getValue()) {
            case LoginController.USER_PROFESSOR, LoginController.USER_REPRESENTATIVE -> {
                gridPaneNewProfessor.setVisible(true);
                gridPaneNewStudent.setVisible(false);
            }
            case LoginController.USER_STUDENT -> {
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
        MainStage.changeView("usermanagement-view.fxml", 1000, 600 + HEIGHT_OFFSET);
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
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
