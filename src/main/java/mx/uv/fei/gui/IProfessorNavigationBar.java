package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IProfessorNavigationBar {
    @FXML
    void redirectToAdvancementManagement();
    @FXML
    void redirectToProjectManagement();
    @FXML
    void redirectToEvidences();
    @FXML
    void redirectToRequests();
    @FXML
    void actionLogOut() throws IOException;
}
