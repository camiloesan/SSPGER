package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProfessorViewProjectsController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private TableView<SimpleProject> tableViewProjects;
    @FXML
    private TableColumn<SimpleProject, Integer> tableColumnProjectID;
    @FXML
    private TableColumn<SimpleProject, String> tableColumnProjectTitle;
    @FXML
    private TableColumn<SimpleProject, String> tableColumnProjectState;
    @FXML
    private HBox hboxLogOutLabel;

    private static final Logger logger = Logger.getLogger(ProfessorViewProjectsController.class);

    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        prepareTableViewProjects();
        try {
            fillProjectTableByRole();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudieron recuperar sus proyectos.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    private void prepareTableViewProjects() {
        tableViewProjects.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableColumnProjectID = new TableColumn<>("ID");
        tableColumnProjectID.setCellValueFactory(new PropertyValueFactory<>("projectID"));

        tableColumnProjectTitle = new TableColumn<>("Título");
        tableColumnProjectTitle.setCellValueFactory(new PropertyValueFactory<>("receptionWorkName"));

        tableColumnProjectState = new TableColumn<>("Estado");
        tableColumnProjectState.setCellValueFactory(new  PropertyValueFactory<>("projectState"));
        tableColumnProjectState.setResizable(false);
        tableColumnProjectState.setMaxWidth(100);
        tableColumnProjectState.setMinWidth(100);

        tableViewProjects.getColumns().clear();
        tableViewProjects.getColumns().addAll(tableColumnProjectTitle,tableColumnProjectState);
    }

    private void fillProjectTableByRole() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        int professorID = Integer.parseInt(SessionDetails.getInstance().getId());
        tableViewProjects.getItems().clear();
        tableViewProjects.getItems().addAll(projectDAO.getProjectsByParticipation(professorID));
    }

    @FXML
    private void refreshTable() {
        try {
            fillProjectTableByRole();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudieron recuperar sus proyectos.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    @FXML
    private void openProjectDetails() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            MainStage.changeView("viewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void openProjectRegistration() throws IOException {
        MainStage.changeView("registerprojectproposal-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }


    @FXML
    private void openFollowUp() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            TransferProject.setReceptionWorkName(tableViewProjects
                    .getSelectionModel()
                    .getSelectedItem()
                    .getReceptionWorkName());
            MainStage.changeView("followup-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un proyecto para ver seguimiento de los alumnos.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void openProjectTimeline() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setReceptionWorkName(tableViewProjects.
                    getSelectionModel()
                    .getSelectedItem()
                    .getReceptionWorkName());
            TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            MainStage.changeView("timeline-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un proyecto para ver el cronograma", AlertStatus.WARNING));
        }
    }

    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
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
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
