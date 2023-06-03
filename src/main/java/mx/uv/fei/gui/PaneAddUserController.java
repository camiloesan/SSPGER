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
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfessorLastName;
    @FXML
    private TextField textFieldProfessorName;
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

    private static final String PROFESSOR_USER = "Profesor";
    private static final String STUDENT__USER = "Estudiante";
    private static final String ACADEMIC_BODY_REPRESENTATIVE_USER = "RepresentanteCA";
    private static final String ADMIN_USER = "Administrador";

    @FXML
    private void initialize() {
        comboBoxDegree.setItems(observableListComboItemsDegree);
        comboBoxUserType.setItems(observableListComboItemsUserType);
    }

    @FXML
    void buttonSaveAction() throws SQLException {
        if (comboBoxUserType.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Debes seleccionar a un tipo de usuario", AlertStatus.WARNING
            ));
        } else {
            String userType = comboBoxUserType.getValue();
            if (areFieldsValid(userType)) {
                switch (userType) {
                    case PROFESSOR_USER, ACADEMIC_BODY_REPRESENTATIVE_USER -> addProfessorUser();
                    case STUDENT__USER -> addStudentUser();
                    case ADMIN_USER -> addAdminUser();
                }
            }
        }
    }

    private void clearFields() {
        comboBoxDegree.setValue(null);
        passwordFieldPassword.setText("");
        textFieldProfessorName.setText("");
        textFieldProfessorLastName.setText("");
        textFieldStudentId.setText("");
        textFieldStudentName.setText("");
        textFieldStudentLastName.setText("");
        textFieldUsername.setText("");
        textFieldEmail.setText("");
    }

    private void addAdminUser() throws SQLException {
        int result = 0;
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccount.setUserEmail(textFieldEmail.getText());

        String newUsername = textFieldUsername.getText();
        String newEmail = textFieldEmail.getText();
        if (userDAO.isUserTaken(newUsername)) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El usuario ya existe, elija otro nombre.", AlertStatus.WARNING));
        } else if (userDAO.isEmailTaken(newEmail)) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El usuario ya existe, elija otro nombre.", AlertStatus.WARNING));
        } else {
            try {
                result = userDAO.addAdminUser(accessAccount);
            } catch (SQLException sqlException) {
                logger.error(sqlException);
            }

            if (result == 0) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Ocurrió un error con la base de datos, inténtelo más tarde", AlertStatus.ERROR));
            } else {
                DialogGenerator.getDialog(new AlertMessage(
                        "Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
                clearFields();
            }
        }
    }

    private void addProfessorUser() throws SQLException {
        boolean result = false;
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccount.setUserEmail(textFieldEmail.getText());
        Professor professor = new Professor();
        professor.setProfessorName(textFieldProfessorName.getText());
        professor.setProfessorLastName(textFieldProfessorLastName.getText());
        professor.setProfessorDegree(comboBoxDegree.getValue());

        if (userDAO.isUserTaken(textFieldUsername.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El usuario ya existe, elija otro nombre.", AlertStatus.ERROR));
        } else {
            try {
                result = userDAO.addProfessorUserTransaction(accessAccount, professor);
            } catch (SQLException sqlException) {
                logger.error(sqlException);
            }
        }

        if (!result) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
            clearFields();
        }
    }

    private void addStudentUser() throws SQLException {
        boolean result = false;
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        StudentDAO studentDAO = new StudentDAO();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        accessAccount.setUserEmail(textFieldEmail.getText());
        Student student = new Student();
        student.setStudentID(textFieldStudentId.getText());
        student.setName(textFieldStudentName.getText());
        student.setLastName(textFieldStudentLastName.getText());

        boolean isStudentIDTaken = false;
        try {
            isStudentIDTaken = studentDAO.isStudentIDTaken(student.getStudentID());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Ocurrió un error con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR
            ));
            logger.error(sqlException);
        }

        boolean modifiedSuccessfully = false;
        if (userDAO.isUserTaken(textFieldStudentId.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El usuario ya se encuentra registrado, no se pudo agregar.", AlertStatus.ERROR));
        } else {
            if (isStudentIDTaken) {
                DialogGenerator.getDialog(new AlertMessage(
                        "La matrícula ya se encuentra registrada.", AlertStatus.ERROR));
            } else {
                try {
                    modifiedSuccessfully = userDAO.addStudentUserTransaction(accessAccount, student);
                } catch (SQLException sqlException) {
                    logger.error(sqlException);
                }
            }

            if (!modifiedSuccessfully) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo añadir al usuario, inténtelo más tarde", AlertStatus.ERROR));
            } else {
                DialogGenerator.getDialog(new AlertMessage(
                        "Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
                clearFields();
            }
        }
    }

    private static final int MAX_LENGTH_USERNAME = 28;
    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 28;
    private static final int MAX_LENGTH_STUDENT_ID = 10;

    private boolean areFieldsValid(String userType) {
        if (areAccessAccountFieldsValid()) {
            switch (userType) {
                case PROFESSOR_USER, ACADEMIC_BODY_REPRESENTATIVE_USER -> {
                    return areProfessorFieldsValid();
                }
                case STUDENT__USER -> {
                    return areStudentFieldsValid();
                }
                default -> {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    private boolean areAccessAccountFieldsValid() {
        Professor professor = new Professor();
        if (textFieldUsername.getText().isBlank()
                || passwordFieldPassword.getText().isBlank()
                || comboBoxUserType.getValue() == null
                || textFieldEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldUsername.getText().length() > MAX_LENGTH_USERNAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El campo usuario debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (passwordFieldPassword.getText().length() > MAX_LENGTH_PASSWORD) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El campo contraseña debe tener máximo 64 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El correo electrónino debe tener máximo 28 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (!professor.isEmailValid(textFieldEmail.getText()) && comboBoxUserType.getValue().equals("Administrador")) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El formato del correo electrónico no es válido, " +
                            "sólo se permiten caracteres alfa-numéricos" +
                            " y direcciones válidas del personal de la Universidad Veracruzana (@uv.mx)",
                    AlertStatus.WARNING
            ));
            return false;
        } else {
            return true;
        }
    }

    private boolean areProfessorFieldsValid() {
        Professor professor = new Professor();
        if (textFieldProfessorName.getText().isBlank()
            || textFieldProfessorLastName.getText().isBlank()
            || comboBoxDegree.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldProfessorName.getText().length() > MAX_LENGTH_NAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el campo nombre del profesor debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldProfessorLastName.getText().length() > MAX_LENGTH_LASTNAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el campo apellidos debe tener máximo 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (!professor.isEmailValid(textFieldEmail.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El formato del correo electrónico no es válido, " +
                            "sólo se permiten caracteres alfa-numéricos" +
                            " y direcciones válidas de la Universidad Veracruzana (@uv.mx)",
                    AlertStatus.WARNING
            ));
            return false;
        } else {
            return true;
        }
    }

    private boolean areStudentFieldsValid() {
        Student student = new Student();
        if (textFieldStudentId.getText().isBlank()
                || textFieldStudentName.getText().isBlank()
                || textFieldStudentLastName.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldStudentId.getText().length() != MAX_LENGTH_STUDENT_ID) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, la matrícula debe tener exactamente 10 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldStudentName.getText().length() > MAX_LENGTH_NAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el límite del nombre es de 30 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (textFieldStudentLastName.getText().length() > MAX_LENGTH_LASTNAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Tamaño inválido, el límite del campo apellidos es de máximo 80 caracteres", AlertStatus.WARNING
            ));
            return false;
        } else if (!student.isEmailValid(textFieldEmail.getText())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El formato del correo electrónico no es válido, " +
                            "sólo se permiten caracteres alfa-numéricos y direcciones válidas" +
                            "de estudiantes de la Universidad Veracruzana (@estudiantes.uv.mx)",
                    AlertStatus.WARNING
            ));
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
