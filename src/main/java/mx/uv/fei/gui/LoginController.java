package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.dao.AccessAccountDAO;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class LoginController {
    @FXML
    private TextField textFieldUser;

    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private void onActionButtonContinue() throws SQLException, IOException, MessagingException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        if (accessAccountDAO.areCredentialsValid(textFieldUser.getText(), textFieldPassword.getText()) || isExternalEmailValid()) {
            redirectToWindow();
            closeCurrentWindow();
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
                CRUDAccessAccountWindow crudAccessAccountWindow = new CRUDAccessAccountWindow();
                crudAccessAccountWindow.start(new Stage());
                break;
            case "Estudiante":
                break;
            case "Profesor":
                break;
            case "RepresentanteCA":
                break;
        }
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) textFieldUser.getScene().getWindow();
        stage.close();
    }
}
