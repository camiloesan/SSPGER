package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IProfessorNavigationBar {
    @FXML
    void redirectToProfessorAdvancementManagement() throws IOException;
    @FXML
    void redirectToProfessorProjectManagement() throws IOException;
    @FXML
    void redirectToProfessorEvidenceManager() throws IOException;
    @FXML
    void redirectToProjectRequests() throws IOException;
    @FXML
    void actionLogOut() throws IOException;
}
