package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.SimpleProject;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProjectProposalsController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelHeader;
    @FXML
    private Button buttonVerDetalles;
    @FXML
    private Label labelFilter;
    @FXML
    private ComboBox<String> comboProjectStates;
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
    @FXML
    private Button buttonAcceptProject;
    @FXML
    private Button buttonDeclineProject;
    
    private static final String ALL_COMBO_OPTION = "Todos";
    private static final String PARTICIPATING_COMBO_OPTION = "Mis proyectos";
    private static final String UNVERIFIED_COMBO_OPTION = "Por revisar";
    private static final String VERIFIED_COMBO_OPTION = "Verificados";
    private static final String DECLINED_COMBO_OPTION = "Declinados";
    private static final String UNVERIFIED_PROJECT_STATE = "Por revisar";
    private static final String VERIFIED_PROJECT_STATE = "Verificado";
    private static final String DECLINED_PROJECT_STATE = "Declinado";
    private static String PROJECT_VALIDATION;
 
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        prepareTableViewProjects();
        if(!isRCA()) {
            fillProjectListByRole();
            tableColumnProjectID.setVisible(false);
            buttonAcceptProject.setVisible(false);
            buttonDeclineProject.setVisible(false);
            labelFilter.setVisible(false);
            comboProjectStates.setVisible(false);
        } else {
            fillProjectStateCombo();
            fillUnfilteredTable();
        }
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public boolean isRCA() {
        return Objects.equals(LoginController.sessionDetails.getUserType(), "RepresentanteCA");
    }
    
    private void prepareTableViewProjects() {
        tableViewProjects.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableColumnProjectID = new TableColumn<>("ID");
        tableColumnProjectID.setCellValueFactory(new PropertyValueFactory<>("projectID"));
        
        tableColumnProjectTitle = new TableColumn<>("Título");
        tableColumnProjectTitle.setCellValueFactory(new PropertyValueFactory<>("projectTitle"));
        
        tableColumnProjectState = new TableColumn<>("Estado");
        tableColumnProjectState.setCellValueFactory(new  PropertyValueFactory<>("projectState"));
        tableColumnProjectState.setResizable(false);
        tableColumnProjectState.setMaxWidth(100);
        tableColumnProjectState.setMinWidth(100);
        
        tableViewProjects.getColumns().addAll(tableColumnProjectTitle,tableColumnProjectState);
    }
    
    private void fillUnfilteredTable() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        tableViewProjects.getItems().clear();
        
        List<SimpleProject> proposedProjects = new ArrayList<>(projectDAO.getAllProjects());
        proposedProjects.forEach(element -> tableViewProjects.getItems().add(element));
    }
    
    @FXML
    private void openProjectTimeline() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setReceptionWorkName(tableViewProjects.getSelectionModel().getSelectedItem().getProjectTitle());
            TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            MainStage.changeView("timeline-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver el cronograma", AlertStatus.WARNING));
        }
    }
    
    public void fillProjectListByRole() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        tableViewProjects.getItems().clear();
        
        ArrayList<SimpleProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByRole(Integer.parseInt(LoginController.sessionDetails.getId())));
        proposedProjects.forEach(element -> tableViewProjects.getItems().add(element));
    }
    
    public void fillFilteredProjects(String projectState) throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        tableViewProjects.getItems().clear();
        
        ArrayList<SimpleProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByState(projectState));
        proposedProjects.forEach(element -> tableViewProjects.getItems().add(element));
    }
    
    public void fillProjectStateCombo() {
        ObservableList<String > projectStates = FXCollections.observableArrayList(
                ALL_COMBO_OPTION,
                PARTICIPATING_COMBO_OPTION,
                UNVERIFIED_COMBO_OPTION,
                VERIFIED_COMBO_OPTION,
                DECLINED_COMBO_OPTION);
        comboProjectStates.setItems(projectStates);
    }
    
    public boolean noFilterSelected() {
        return comboProjectStates.getValue() == null;
    }
    
    public void refreshFilteredList() throws SQLException {
        if (noFilterSelected()) {
            if(isRCA()){
                fillUnfilteredTable();
            } else {
                fillProjectListByRole();
            }
        } else{
            String selectedItem = comboProjectStates.getSelectionModel().getSelectedItem();
            switch (selectedItem) {
                case ALL_COMBO_OPTION -> fillUnfilteredTable();
                case PARTICIPATING_COMBO_OPTION -> fillProjectListByRole();
                case UNVERIFIED_COMBO_OPTION -> fillFilteredProjects(UNVERIFIED_PROJECT_STATE);
                case VERIFIED_COMBO_OPTION -> fillFilteredProjects(VERIFIED_PROJECT_STATE);
                case DECLINED_COMBO_OPTION -> fillFilteredProjects(DECLINED_PROJECT_STATE);
            }
            labelHeader.setText(comboProjectStates.getSelectionModel().getSelectedItem());
        }
    }
    
    public void openProjectDetails() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            int projectID = (tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            TransferProject.setProjectID(projectID);
            MainStage.changeView("viewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void acceptProject() {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Verificado";
            try {
                projectDAO.updateProjectState(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID(), PROJECT_VALIDATION);
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Aceptarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void declineProject() {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Declinado";
            try {
                projectDAO.updateProjectState((tableViewProjects.getSelectionModel().getSelectedItem().getProjectID()), PROJECT_VALIDATION);
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Rechazarlo", AlertStatus.WARNING));
        }
    }
    
    @FXML
    public void openProjectRegistration() throws IOException {
        try {
            MainStage.changeView("registerprojectproposal-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        try {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
