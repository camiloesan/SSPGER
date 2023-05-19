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
import mx.uv.fei.logic.*;

import java.sql.SQLException;
import java.io.IOException;
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
    @FXML
    private Button buttonSeguimiento;
    
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
        buttonSeguimiento.setVisible(false);
        if(!isRCA()) {
            fillProjectTableByRole();
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
    
    private boolean isRCA() {
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
        
        tableViewProjects.getColumns().clear();
        tableViewProjects.getColumns().addAll(tableColumnProjectTitle,tableColumnProjectState);
    }
    
    private void fillUnfilteredTable() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        buttonSeguimiento.setVisible(false);
        tableViewProjects.getItems().clear();
        tableViewProjects.getItems().addAll(projectDAO.getAllProjects());
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
    
    private void fillProjectTableByRole() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        buttonSeguimiento.setVisible(true);
        tableViewProjects.getItems().clear();
        tableViewProjects.getItems().addAll(projectDAO.getProjectsByCollaboration(Integer.parseInt(LoginController.sessionDetails.getId())));
    }
    
    private void fillFilteredProjects(String projectState) throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        buttonSeguimiento.setVisible(false);
        tableViewProjects.getItems().clear();
        tableViewProjects.getItems().addAll(projectDAO.getProjectsByState(projectState));
    }
    
    private void fillProjectStateCombo() {
        ObservableList<String > projectStates = FXCollections.observableArrayList(
                ALL_COMBO_OPTION,
                PARTICIPATING_COMBO_OPTION,
                UNVERIFIED_COMBO_OPTION,
                VERIFIED_COMBO_OPTION,
                DECLINED_COMBO_OPTION);
        comboProjectStates.setItems(projectStates);
    }
    
    private boolean noFilterSelected() {
        return comboProjectStates.getValue() == null;
    }
    
    @FXML
    private void refreshFilteredTable() throws SQLException {
        if (noFilterSelected()) {
            if(isRCA()){
                fillUnfilteredTable();
            } else {
                fillProjectTableByRole();
            }
        } else{
            String selectedItem = comboProjectStates.getSelectionModel().getSelectedItem();
            switch (selectedItem) {
                case ALL_COMBO_OPTION -> fillUnfilteredTable();
                case PARTICIPATING_COMBO_OPTION -> fillProjectTableByRole();
                case UNVERIFIED_COMBO_OPTION -> fillFilteredProjects(UNVERIFIED_PROJECT_STATE);
                case VERIFIED_COMBO_OPTION -> fillFilteredProjects(VERIFIED_PROJECT_STATE);
                case DECLINED_COMBO_OPTION -> fillFilteredProjects(DECLINED_PROJECT_STATE);
            }
            labelHeader.setText(comboProjectStates.getSelectionModel().getSelectedItem());
        }
    }
    
    @FXML
    private void openProjectDetails() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
            MainStage.changeView("viewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }
    
    @FXML
    private void openFollowUp() throws IOException {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
        TransferProject.setProjectID(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID());
        TransferProject.setReceptionWorkName(tableViewProjects.getSelectionModel().getSelectedItem().getProjectTitle());
        MainStage.changeView("followup-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver seguimiento de los alumnos.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void acceptProject() {
        if (tableViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Verificado";
            try {
                projectDAO.updateProjectState(tableViewProjects.getSelectionModel().getSelectedItem().getProjectID(), PROJECT_VALIDATION);
                refreshFilteredTable();
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
                refreshFilteredTable();
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Rechazarlo", AlertStatus.WARNING));
        }
    }
    
    @FXML
    private void openProjectRegistration() throws IOException {
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
    
    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
