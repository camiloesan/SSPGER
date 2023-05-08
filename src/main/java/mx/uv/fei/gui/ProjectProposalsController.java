package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ProjectProposalsController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelHeader;
    @FXML
    public Button buttonVerDetalles;
    @FXML
    private ComboBox<String> comboProjectStates;
    @FXML
    private ListView<String> listViewProjects;
    @FXML
    private HBox hboxLogOutLabel;
    
    private static final String ALL_COMBO_OPTION = "Todos";
    private static final String UNVERIFIED_COMBO_OPTION = "Por revisar";
    private static final String VERIFIED_COMBO_OPTION = "Verificados";
    private static final String DECLINED_COMBO_OPTION = "Declinados";
    private static final String UNVERIFIED_PROJECT_STATE = "Por revisar";
    private static final String VERIFIED_PROJECT_STATE = "Verificado";
    private static final String DECLINED_PROJECT_STATE = "Declinado";
    private static String PROJECT_VALIDATION;
 
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillProjectStateCombo();
        fillUnfilteredList();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void fillUnfilteredList() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getAllProjects());
        proposedProjects.forEach(element -> listViewProjects.getItems().add(element.getProjectTitle()));
    }
    
    public void fillFilteredProjects(String projectState) throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByState(projectState));
        proposedProjects.forEach(element -> listViewProjects.getItems().add(element.getProjectTitle()));
    }
    
    public void fillProjectStateCombo() {
        ObservableList<String > projectStates = FXCollections.observableArrayList(ALL_COMBO_OPTION,UNVERIFIED_COMBO_OPTION,VERIFIED_COMBO_OPTION,DECLINED_COMBO_OPTION);
        comboProjectStates.setItems(projectStates);
    }
    
    public boolean noFilterSelected() {
        return comboProjectStates.getValue() == null;
    }
    
    public void refreshFilteredList() throws SQLException {
        if (noFilterSelected()) {
            DialogGenerator.getDialog(new AlertMessage("Se debe especificar un filtro", AlertStatus.WARNING));
        } else{
            if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), ALL_COMBO_OPTION)) {
                fillUnfilteredList();
            } else if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), UNVERIFIED_COMBO_OPTION)){
                fillFilteredProjects(UNVERIFIED_PROJECT_STATE);
            } else if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), VERIFIED_COMBO_OPTION)) {
                fillFilteredProjects(VERIFIED_PROJECT_STATE);
            } else {
                fillFilteredProjects(DECLINED_PROJECT_STATE);
            }
            labelHeader.setText(comboProjectStates.getSelectionModel().getSelectedItem());
        }
    }
    
    public void openProjectDetails() throws IOException {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            String receptionWorkName = listViewProjects.getSelectionModel().getSelectedItem();
            TransferProject.setReceptionWorkName(receptionWorkName);
            MainStage.changeView("viewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void acceptProject() {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Verificado";
            try {
                projectDAO.updateProjectState(projectDAO.getProjectIDByTitle(listViewProjects
                                .getSelectionModel()
                                .getSelectedItem())
                        , PROJECT_VALIDATION);
            } catch (SQLException requestException) {
                requestException.printStackTrace();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para Aceptarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void declineProject() {
        if (listViewProjects.getSelectionModel().getSelectedItem() != null) {
            ProjectDAO projectDAO = new ProjectDAO();
            PROJECT_VALIDATION = "Declinado";
            try {
                projectDAO.updateProjectState(projectDAO.getProjectIDByTitle(listViewProjects
                                .getSelectionModel()
                                .getSelectedItem())
                        , PROJECT_VALIDATION);
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
    public void redirectToAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToEvidences() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToRequests() throws IOException {
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
