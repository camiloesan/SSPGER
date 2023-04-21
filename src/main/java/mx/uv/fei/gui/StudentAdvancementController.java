package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Optional;

public class StudentAdvancementController {
    @FXML
    private Rectangle optionAdvancements;
    @FXML
    private Rectangle optionEvidences;
    @FXML
    private Rectangle optionProjects;
    @FXML
    private Rectangle optionRequest;
    private static final double SELECTED_OPACITY = 0.16;

    @FXML
    private void actionLogOut() throws IOException {
        logOut();
    }

    @FXML
    private void actionAdvancements() {
        optionAdvancements.setOpacity(SELECTED_OPACITY);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
    }

    @FXML
    private void actionEvidences() {
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(SELECTED_OPACITY);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
    }

    @FXML
    private void actionProjects() {
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(SELECTED_OPACITY);
        optionRequest.setOpacity(0);
    }

    @FXML
    private void actionRequest() {
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(SELECTED_OPACITY);
    }

    @FXML
    private void initialize() {
        optionAdvancements.setOpacity(SELECTED_OPACITY);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
    }

    private void logOut() throws IOException {
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
