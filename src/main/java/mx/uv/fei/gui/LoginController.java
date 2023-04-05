package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.AccessAccountDAO;

import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField textFieldUser;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private void onActionButtonContinue() throws SQLException {

        AccessAccount accessAccount = new AccessAccount(textFieldUser.getText(), textFieldPassword.getText());
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        accessAccountDAO.addAccessAccount(accessAccount);

        Stage stage = (Stage) textFieldUser.getScene().getWindow();
        stage.close();
    }
}
