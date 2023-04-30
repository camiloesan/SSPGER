package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;

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
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        proposedProjects.forEach(element -> listViewProjectProposals.getItems().add(element.getProjectTitle()));
    }
    
    public void openProjectDetails() throws SQLException {
    
    }
}
