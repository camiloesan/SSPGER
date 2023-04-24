package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

import mx.uv.fei.dao.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ListProjectProposalsController {
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
        ObservableList<String > projectStates = FXCollections.observableArrayList("Todos","Por revisar","Verificados","Declinados");
        comboProjectStates.setItems(projectStates);
    }
    
    public boolean noFilterSelected() {
        return comboProjectStates.getValue() == null;
    }
    
    public void refreshFilteredList() throws SQLException{
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        if (noFilterSelected()) {
            alert.setTitle("Sin filtro");
            alert.setContentText("Debe especificar un filtro");
            alert.showAndWait();
        } else if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), "Todos")) {
            fillUnfilteredList();
        } else if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), "Por revisar")){
            fillFilteredProjects("Por revisar");
        } else if (Objects.equals(comboProjectStates.getSelectionModel().getSelectedItem(), "Verificados")) {
            fillFilteredProjects("Verificado");
        } else {
            fillFilteredProjects("Declinado");
        }
        labelHeader.setText(comboProjectStates.getSelectionModel().getSelectedItem());
    }
    
    public void openProjectDetails() throws SQLException {
    
    }
}
