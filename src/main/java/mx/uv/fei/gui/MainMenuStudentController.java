package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.shape.Rectangle;

public class MainMenuStudentController {
    @FXML
    private Rectangle optionAdvancements;

    @FXML
    private Rectangle optionEvidences;

    @FXML
    private Rectangle optionProjects;

    @FXML
    private Rectangle optionRequest;

    @FXML
    private TabPane paneAdvancements;

    @FXML
    private TabPane paneEvidences;

    @FXML
    private TabPane paneProjects;

    @FXML
    private TabPane paneRequest;

    private void handleStyling(Rectangle rectangle) {
        rectangle.setOpacity(0.16);
    }

    @FXML
    private void actionAdvancements() {
        handleStyling(optionAdvancements);
        optionAdvancements.setOpacity(0.16);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
        paneAdvancements.toFront();
    }

    @FXML
    private void actionEvidences() {
        handleStyling(optionEvidences);
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(0.16);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
        paneEvidences.toFront();
    }

    @FXML
    private void actionProjects() {
        handleStyling(optionProjects);
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0.16);
        optionRequest.setOpacity(0);
        paneProjects.toFront();
    }

    @FXML
    private void actionRequest() {
        handleStyling(optionRequest);
        optionAdvancements.setOpacity(0);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0.16);
        paneRequest.toFront();
    }

    @FXML
    private void initialize() {
        optionAdvancements.setOpacity(0.16);
        optionEvidences.setOpacity(0);
        optionProjects.setOpacity(0);
        optionRequest.setOpacity(0);
        paneAdvancements.toFront();
    }

}
