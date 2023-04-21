package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IStudentNavigationBar {
    @FXML
    void actionAdvancements();
    @FXML
    void actionEvidences();
    @FXML
    void actionProjects();
    @FXML
    void actionRequest();
    @FXML
    void actionLogOut() throws IOException;
}
