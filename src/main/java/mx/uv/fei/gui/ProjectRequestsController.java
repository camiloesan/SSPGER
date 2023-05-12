package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.ProjectRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProjectRequestsController implements IProfessorNavigationBar {
    @FXML
    Label labelUsername;
    @FXML
    Text textMotive;
    @FXML
    TableView<ProjectRequest> tableViewRequests;
    @FXML
    Button buttonAccept;
    @FXML
    Button buttonReject;
    @FXML
    Text textDescription;
    private static String VALIDATION_REQUEST;

    @FXML
    private void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        TableColumn<ProjectRequest, String> studentIdColumn = new TableColumn<>("Matrícula");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProjectRequest, String> projectColumn = new TableColumn<>("Estado");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequests.getColumns().addAll(studentIdColumn, projectColumn);
        try {
            fillTableViewProjectRequests();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo conectar con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }

        /*
        Runnable helloRunnable = () -> {
            try {
                fillTableViewProjectRequests();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
        */
    }

    @FXML
    private void handleItemClick() {
        if (tableViewRequests.getSelectionModel().getSelectedItem() != null) {
            StudentDAO studentDAO = new StudentDAO();
            ProjectDAO projectDAO = new ProjectDAO();
            String studentName = "";
            String projectName = "";
            try {
                studentName = studentDAO.getNamebyStudentID(tableViewRequests.getSelectionModel().getSelectedItem().getStudentId());
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No se pudo obtener la información del estudiante, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            }
            try {
                projectName = projectDAO.getProjectNameById(tableViewRequests.getSelectionModel().getSelectedItem().getProjectID());
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No se pudo obtener la información del proyecto, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            }

            textDescription.setText("Motivos del estudiante [" + studentName + "] para el proyecto [" + projectName + "]");

            buttonAccept.setVisible(true);
            buttonReject.setVisible(true);
            ProjectRequest projectRequest = tableViewRequests.getSelectionModel().getSelectedItem();
            textMotive.setText(projectRequest.getDescription());
        }
    }

    @FXML
    private void acceptRequest() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        VALIDATION_REQUEST = "Aceptado";
        try {
            projectRequestDAO.validateProjectRequest(VALIDATION_REQUEST, tableViewRequests
                    .getSelectionModel()
                    .getSelectedItem()
                    .getProjectPetitionID());
        } catch (SQLException requestException) {
            requestException.printStackTrace();
        }
        tableViewRequests.getItems().clear();
        try {
            fillTableViewProjectRequests();
        } catch (SQLException tableException) {
            tableException.printStackTrace();
        }
    }

    @FXML
    private void rejectRequest() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        VALIDATION_REQUEST = "Rechazado";
        try {
            projectRequestDAO.validateProjectRequest(VALIDATION_REQUEST,tableViewRequests
                    .getSelectionModel()
                    .getSelectedItem()
                    .getProjectPetitionID());
        } catch (SQLException requestException) {
            requestException.printStackTrace();
        }
        tableViewRequests.getItems().clear();
        try {
            fillTableViewProjectRequests();
        } catch (SQLException tableException) {
            tableException.printStackTrace();
        }
    }

    private void fillTableViewProjectRequests() throws SQLException {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        tableViewRequests.getItems().clear();
        tableViewRequests.getItems().addAll(projectRequestDAO.getProjectRequestsListByProfessorId(Integer.parseInt(LoginController.sessionDetails.getId())));
    }

    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectRequests() {

    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if(confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
