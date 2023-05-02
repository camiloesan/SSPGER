package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewProjectProposalsController {
    @FXML
    private Label labelHeader;
    @FXML
    private Button buttonActualizar;
    @FXML
    public Button buttonVerDetalles;
    @FXML
    private ComboBox<String> comboProjectStates;
    @FXML
    private ListView<String> listViewProjects;
    
    private static final String ALL_COMBO_OPTION = "Todos";
    private static final String UNVERIFIED_COMBO_OPTION = "Por revisar";
    private static final String VERIFIED_COMBO_OPTION = "Verificados";
    private static final String DECLINED_COMBO_OPTION = "Declinados";
    private static final String UNVERIFIED_PROJECT_STATE = "Por revisar";
    private static final String VERIFIED_PROJECT_STATE = "Verificado";
    private static final String DECLINED_PROJECT_STATE = "Declinado";
 
    public void initialize() throws SQLException {
        fillProjectStateCombo();
        fillUnfilteredList();
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
}
