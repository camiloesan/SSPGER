package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;

public interface IStudentNavigationBar {
    @FXML
    void redirectToAdvancements() throws IOException;
    @FXML
    void redirectToEvidences() throws IOException, SQLException;
    @FXML
    void redirectToProjects() throws  IOException;
    @FXML
    void redirectToRequest() throws IOException;
    @FXML
    void actionLogOut() throws IOException;
}
