package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Optional;

public class StudentAdvancementController implements IStudentNavigationBar {
    @FXML
    private Rectangle optionAdvancements;
    private static final double SELECTED_OPACITY = 0.16;

    @FXML
    private void initialize() {
        optionAdvancements.setOpacity(SELECTED_OPACITY);
    }

    @Override
    public void actionAdvancements() {
        //nun
    }

    @Override
    public void actionEvidences() {
        //change view
    }

    @Override
    public void actionProjects() {

    }

    @Override
    public void actionRequest() {

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
