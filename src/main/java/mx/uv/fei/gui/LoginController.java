package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.AccessAccountDAO;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import mx.uv.fei.logic.TelegramMessenger;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class LoginController {
    @FXML
    private TextField textFieldUser;
    @FXML
    private PasswordField textFieldPassword;
    private static final Logger logger = Logger.getLogger(LoginController.class);
    private static final int HEIGHT_OFFSET = 44;

    @FXML
    private void onActionButtonContinue() throws IOException, SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();

        boolean isLoginValid = false;
        try {
            isLoginValid = accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText());
        } catch (SQLException sqlException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error con la base de datos");
            alert.setContentText("No se pudo conectar a la base de datos, inténtelo de nuevo más tarde");
            alert.show();
            TelegramMessenger.sendToTelegram(sqlException + " " + sqlException.getMessage());
            logger.error("Error en login: " + sqlException);
        }

        if (isLoginValid) {
            redirectToWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("El usuario o contraseña no son válidos");
            alert.setContentText("Inténtelo de nuevo");
            alert.showAndWait();
        }
    }

    public boolean isExternalEmailValid() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.pop3.starttls.enable", "true");
        Session mailSession = Session.getInstance(props);
        mailSession.setDebug(true);
        Store mailStore = mailSession.getStore("pop3");
        mailStore.connect("outlook.office365.com", textFieldUser.getText(), textFieldPassword.getText());
        return mailStore.isConnected();
    }

    private void redirectToWindow() throws SQLException, IOException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        switch (accessAccountDAO.getAccessAccountTypeByUsername(textFieldUser.getText())) {
            case "Administrador":
                MainStage.changeView("crudaccessaccount-view.fxml", 800, 500 + HEIGHT_OFFSET);
                break;
            case "Estudiante":
                MainStage.changeView("studentadvancement-view.fxml", 800, 500 + HEIGHT_OFFSET);
                break;
            case "Profesor":
                break;
            case "RepresentanteCA":
                break;
        }
    }
}
