package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Optional;

public class StudentAdvancementController implements IStudentNavigationBar {

    @FXML
    private void initialize() {

    }

    @Override
    public void redirectToAdvancements() {

    }

    @Override
    public void redirectToEvidences() throws  IOException {
            MainStage.changeView("studentevidences-view.fxml", 800, 500);
    }

    @Override
    public void redirectToProjects() {

    }

    @Override
    public void redirectToRequest() {

    }

    @Override
    public void actionLogOut() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            //reset credentials
        }
    }
}
