package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.Project;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListProjectProposalsController {
    
    @FXML
    public Button buttonVerDetalles;
    @FXML
    private ListView<String> listViewProjectProposals;
    @FXML
    private Button buttonActualizar;
    @FXML
    private Button buttonRegresar;
    
    public void initialize() throws SQLException {
        fillListView();
    }
    
    public void fillListView() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewProjectProposals.getItems().clear();
        
        ArrayList<DetailedProject> detailedProjects = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        
        detailedProjects.forEach(element -> listViewProjectProposals.getItems().add(element.getProjectTitle()));
    }
}
