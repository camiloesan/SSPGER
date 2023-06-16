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
import mx.uv.fei.logic.SessionDetails;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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
    private static final String ACCEPT_REQUEST = "Aceptado";
    private static final String DECLINE_REQUEST = "Rechazado";
    private static final Logger logger = Logger.getLogger(ProjectRequestsController.class);

    @FXML
    private void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        TableColumn<ProjectRequest, String> studentIdColumn = new TableColumn<>("Matrícula");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProjectRequest, String> projectColumn = new TableColumn<>("Estado");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequests.getColumns().addAll(Arrays.asList(studentIdColumn, projectColumn));
        try {
            fillTableViewProjectRequests();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo conectar con la base de datos", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    @FXML
    private void handleItemClick() {
        if (tableViewRequests.getSelectionModel().getSelectedItem() != null) {
            StudentDAO studentDAO = new StudentDAO();
            ProjectDAO projectDAO = new ProjectDAO();
            String studentName = "";
            String projectName = "";
            try {
                studentName = studentDAO.getNamebyStudentID(tableViewRequests
                        .getSelectionModel()
                        .getSelectedItem()
                        .getStudentId());
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo obtener la información del estudiante", AlertStatus.ERROR));
                logger.error(sqlException);
            }
            try {
                projectName = projectDAO.getProjectNameById(tableViewRequests
                        .getSelectionModel()
                        .getSelectedItem()
                        .getProjectID());
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo obtener la información del proyecto", AlertStatus.ERROR));
                logger.error(sqlException);
            }

            textDescription
                    .setText("Motivos del estudiante [" + studentName + "] para el proyecto [" + projectName + "]");

            buttonAccept.setVisible(true);
            buttonReject.setVisible(true);
            ProjectRequest projectRequest = tableViewRequests.getSelectionModel().getSelectedItem();
            textMotive.setText(projectRequest.getDescription());
        }
    }

    @FXML
    private void acceptRequest() {
        validateProjectRequest(ACCEPT_REQUEST);
    }

    private void validateProjectRequest(String validation) {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        if (projectRequestIsSelected()) {
            try {
                projectRequestDAO.validateProjectRequest(validation, tableViewRequests
                        .getSelectionModel()
                        .getSelectedItem()
                        .getProjectPetitionID());
            } catch (SQLException requestException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo validar el proyecto", AlertStatus.ERROR));
                logger.error(requestException);
            }
            tableViewRequests.getItems().clear();
            try {
                fillTableViewProjectRequests();
            } catch (SQLException tableException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo obtener las solicitudes del proyecto", AlertStatus.ERROR));
                logger.error(tableException);
            }
        }
    }

    @FXML
    private void rejectRequest() {
        validateProjectRequest(DECLINE_REQUEST);
    }

    private void fillTableViewProjectRequests() throws SQLException {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        tableViewRequests.getItems().clear();
        tableViewRequests.getItems().addAll(projectRequestDAO.
                getProjectRequestsListByProfessorId(Integer.parseInt(SessionDetails.getInstance().getId())));
    }

    private boolean projectRequestIsSelected() {
        boolean result;
        if (tableViewRequests.getSelectionModel().getSelectedItem() == null) {
            DialogGenerator.getDialog(new AlertMessage("Selecccione una petición", AlertStatus.WARNING));
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (Objects.equals(LoginController.sessionDetails.getUserType(), "RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (Objects.equals(LoginController.sessionDetails.getUserType(), "Profesor")){
            MainStage.changeView(
                    "professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectRequests() {

    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if(confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
