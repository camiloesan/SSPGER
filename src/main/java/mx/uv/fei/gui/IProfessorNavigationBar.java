package mx.uv.fei.gui;

import javafx.fxml.FXML;

import java.io.IOException;

public interface IProfessorNavigationBar {
    @FXML
    void actionRequests();
    @FXML
    void actionLogOut() throws IOException;
}
