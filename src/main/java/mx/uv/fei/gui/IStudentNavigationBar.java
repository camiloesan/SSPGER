package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IStudentNavigationBar {
    @FXML
    void redirectToAdvancements();
    @FXML
    void redirectToEvidences();
    @FXML
    void redirectToProjects();
    @FXML
    void redirectToRequest();
    @FXML
    void actionLogOut() throws IOException;
}
