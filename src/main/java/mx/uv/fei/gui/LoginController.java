package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.AccessAccountDAO;

import mx.uv.fei.logic.SessionDetails;
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
            logger.error("Error en login: " + sqlException);
        }
    }

    private void continueLogin(boolean isLoginValid) throws SQLException, IOException {
        if (isLoginValid) {
            redirectToWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("El usuario o contraseña no son válidos");
            alert.setContentText("Inténtelo de nuevo");
            alert.showAndWait();
        }
    }

    private void redirectToWindow() throws SQLException, IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        String userType = accessAccountDAO.getAccessAccountTypeByUsername(textFieldUser.getText());
        sessionDetails = SessionDetails.getInstance();
        sessionDetails.setUsername(textFieldUser.getText());
        sessionDetails.setUserType(userType);
        switch (userType) {
            case "Administrador":
                MainStage.changeView("accessaccountmanagement-view.fxml", 800, 500 + HEIGHT_OFFSET);
                break;
            case "Estudiante":
                MainStage.changeView("studentadvancement-view.fxml", 800, 500 + HEIGHT_OFFSET);
                break;
            case "Profesor":
                MainStage.changeView("advancementsmanagement-view.fxml", 800, 500 + HEIGHT_OFFSET);
                break;
            case "RepresentanteCA":
                MainStage.changeView("viewprojectproposals-view.fxml",800,600);
                break;
        }
    }
}
