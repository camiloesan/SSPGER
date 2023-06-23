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
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        TableColumn<ProjectRequest, String> studentIdColumn = new TableColumn<>("Matrícula");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProjectRequest, String> projectColumn = new TableColumn<>("Estado");
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewRequests.getColumns().addAll(Arrays.asList(studentIdColumn, projectColumn));
        fillTableViewProjectRequests();
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
                        "No hay conexión a la base de datos, no se pudo obtener la información del estudiante",
                        AlertStatus.ERROR));
                logger.error(sqlException);
            }
            try {
                projectName = projectDAO.getProjectNameById(tableViewRequests
                        .getSelectionModel()
                        .getSelectedItem()
                        .getProjectID());
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No hay conexión a la base de datos, no se pudo obtener la información del proyecto",
                        AlertStatus.ERROR));
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
        int projectID = tableViewRequests.getSelectionModel().getSelectedItem().getProjectID();
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        if (projectRequestIsSelected()) {
            if (Objects.equals(validation, ACCEPT_REQUEST)) {
                try {
                    projectRequestDAO.validateProjectRequest(validation, tableViewRequests
                            .getSelectionModel()
                            .getSelectedItem()
                            .getProjectPetitionID());
                    projectDAO.decreaseStudentQuota(projectID);
                } catch (SQLException requestException) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "No hay conexión a la base de datos, no se pudo validar la solicitud de proyecto",
                            AlertStatus.ERROR));
                    logger.error(requestException);
                }
            } else if (Objects.equals(validation, DECLINE_REQUEST)) {
                try {
                    projectRequestDAO.validateProjectRequest(validation, tableViewRequests
                            .getSelectionModel()
                            .getSelectedItem()
                            .getProjectPetitionID());
                    if (projectDAO.getAvailableSpaces(projectID) < projectDAO.getStudentQuota(projectID)){
                        projectDAO.increaseStudentQuota(projectID);
                    }
                } catch (SQLException requestException) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "No hay conexión a la base de datos, no se pudo validar la solicitud de proyecto",
                            AlertStatus.ERROR));
                    logger.error(requestException);
                }
            }
            fillTableViewProjectRequests();
        }
    }

    @FXML
    private void rejectRequest() {
        validateProjectRequest(DECLINE_REQUEST);
    }

    private void fillTableViewProjectRequests() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        tableViewRequests.getItems().clear();
        try {
            tableViewRequests.getItems().addAll(projectRequestDAO.
                    getProjectRequestsListByProfessorId(Integer.parseInt(SessionDetails.getInstance().getId())));
        } catch (SQLException tableException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo obtener las solicitudes del proyecto", AlertStatus.ERROR));
            logger.error(tableException);
        }
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
        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_REPRESENTATIVE)) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_PROFESSOR)){
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
