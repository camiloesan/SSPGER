package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.implementations.UserDAO;

import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField textFieldUser;
    @FXML
    private PasswordField textFieldPassword;

    private static final Logger logger = Logger.getLogger(LoginController.class);
    public static final String USER_ADMIN = "Administrador";
    public static final String USER_STUDENT = "Estudiante";
    public static final String USER_PROFESSOR = "Profesor";
    public static final String USER_REPRESENTATIVE = "RepresentanteCA";

    @FXML
    private void onActionButtonContinue() throws IOException {
        UserDAO accessAccountDAO = new UserDAO();
        try {
            continueLogin(accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText()));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo conectar a la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            logger.error("Error en login: " + sqlException);
        }
    }

    private void continueLogin(boolean isLoginValid) throws SQLException, IOException {
        if (isLoginValid) {
            redirectToWindow();
        } else {
            DialogGenerator.getDialog(new AlertMessage("El usuario o contraseña no son válidos", AlertStatus.WARNING));
        }
    }

    static SessionDetails sessionDetails;
    private void redirectToWindow() throws SQLException, IOException {
        UserDAO accessAccountDAO = new UserDAO();
        String userType = accessAccountDAO.getAccessAccountTypeByUsername(textFieldUser.getText());
        String username = textFieldUser.getText();

        switch (userType) {
            case USER_ADMIN -> MainStage.changeView("usermanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            case USER_STUDENT -> {
                StudentDAO studentDAO = new StudentDAO();
                String studentId = studentDAO.getStudentIdByUsername(username);
                sessionDetails = SessionDetails.getInstance(username, userType, studentId);
                MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            }
            case USER_PROFESSOR -> {
                ProfessorDAO professorDAO = new ProfessorDAO();
                String professorId = String.valueOf(professorDAO.getProfessorIdByUsername(username));
                sessionDetails = SessionDetails.getInstance(username, userType, professorId);
                System.out.println(System.identityHashCode(sessionDetails));
                MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            }
            case USER_REPRESENTATIVE -> {
                ProfessorDAO professorDAO = new ProfessorDAO();
                String professorId = String.valueOf(professorDAO.getProfessorIdByUsername(username));
                sessionDetails = SessionDetails.getInstance(username, userType, professorId);
                MainStage.changeView("projectproposals-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            }
        }
    }
}
