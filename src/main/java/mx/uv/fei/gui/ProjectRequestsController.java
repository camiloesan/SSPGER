package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.ProjectRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProjectRequestsController implements IProfessorNavigationBar {
    @FXML
    Label labelDescription;
    @FXML
    Text textMotive;
    @FXML
    TableView<ProjectRequest> tableViewRequests;
    @FXML
    Button buttonAccept;
    @FXML
    Button buttonReject;

    @FXML
    private void initialize() {
        try {
            fillTableViewProjectRequests();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @FXML
    private void handleItemClick() {
        labelDescription.setVisible(true);
        buttonAccept.setVisible(true);
        buttonReject.setVisible(true);
        ProjectRequest projectRequest = tableViewRequests.getSelectionModel().getSelectedItem();
        textMotive.setText(projectRequest.getDescription());
    }

    private void fillTableViewProjectRequests() throws SQLException {
        TableColumn<ProjectRequest, String> studentIdColumn = new TableColumn<>("Matrícula");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProjectRequest, String> projectColumn = new TableColumn<>("Estado");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequests.getColumns().addAll(studentIdColumn, projectColumn);
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        tableViewRequests.getItems().addAll(projectRequestDAO.getProjectRequestsListByProfessorId(Integer.parseInt(LoginController.sessionDetails.getId())));
    }

    @Override
    public void redirectToAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() {

    }

    @Override
    public void redirectToRequests() {

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
