package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.ProfessorDAO;
import mx.uv.fei.dao.ProjectDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterProjectProposalController {
    
    @FXML
    private TextArea textAreaInvestigationProjectName;
    @FXML
    private ComboBox<String> comboLGAC;
    @FXML
    private TextArea textAreaInvestigationLine;
    @FXML
    private TextField textFieldAproxDuration;
    @FXML
    private TextArea textAreaReceptionWorkName;
    @FXML
    private ComboBox<String> comboDirectors;
    @FXML
    private ComboBox<String> comboCodirectors;
    
    public void initialize() throws SQLException {
        fillLgacCombo();
        fillProfessorsCombos();
    }
    
    public void fillLgacCombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> lgacComboItems = FXCollections.observableArrayList();
        List<String> lgacList = new ArrayList<>(projectDAO.getLgacList());
        lgacComboItems.addAll(lgacList);
        
        comboLGAC.setItems(lgacComboItems);
    }
    
    public void fillProfessorsCombos() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        ObservableList<String> professorsNames = FXCollections.observableArrayList();
        List<String> professorsList = new ArrayList<>(professorDAO.getProfessorsNames());
        professorsNames.addAll(professorsList);
        
        comboDirectors.setItems(professorsNames);
        comboCodirectors.setItems(professorsNames);
    }
    
    public void register() throws SQLException {
    
    }
}
