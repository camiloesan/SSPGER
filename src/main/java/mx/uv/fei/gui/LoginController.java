package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.implementations.AccessAccountDAO;

import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.SessionDetails;
import mx.uv.fei.logic.Student;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField textFieldUser;
    @FXML
    private PasswordField textFieldPassword;
    private static final Logger logger = Logger.getLogger(LoginController.class);
    private static final int HEIGHT_OFFSET = 44;
    static SessionDetails sessionDetails;

    @FXML
    private void onActionButtonContinue() throws IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        try {
            continueLogin(accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText()));
        } catch (SQLException sqlException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error con la base de datos");
            alert.setContentText("No se pudo conectar a la base de datos, inténtelo de nuevo más tarde");
            alert.show();
            sqlException.printStackTrace();
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

    private void redirectToWindow() throws SQLException, IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        String userType = accessAccountDAO.getAccessAccountTypeByUsername(textFieldUser.getText());
        sessionDetails = SessionDetails.getInstance();
        sessionDetails.setUsername(textFieldUser.getText());
        sessionDetails.setUserType(userType);
        switch (userType) {
            case "Administrador" -> MainStage.changeView("accessaccountmanagement-view.fxml", 1000, 600 + HEIGHT_OFFSET);
            case "Estudiante" -> {
                StudentDAO studentDAO = new StudentDAO();
                sessionDetails.setId(studentDAO.getStudentIdByUsername(textFieldUser.getText()));
                MainStage.changeView("studentadvancement-view.fxml", 900, 600 + HEIGHT_OFFSET);
            }
            case "Profesor" -> {
                ProfessorDAO professorDAO = new ProfessorDAO();
                sessionDetails.setId(String.valueOf(professorDAO.getProfessorIdByUsername(textFieldUser.getText())));
                MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + HEIGHT_OFFSET);
            }
            case "RepresentanteCA" -> MainStage.changeView("projectproposals-view.fxml", 1000, 700);
        }
    }
}
