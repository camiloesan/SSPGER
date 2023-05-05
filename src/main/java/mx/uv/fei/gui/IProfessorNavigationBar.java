package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IProfessorNavigationBar {
    @FXML
    void redirectToAdvancementManagement() throws IOException;
    @FXML
    void redirectToProjectManagement() throws IOException;
    @FXML
    void redirectToEvidences() throws IOException;
    @FXML
    void redirectToRequests() throws IOException;
    @FXML
    void actionLogOut() throws IOException;
}
