package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;

import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterProjectProposalController {
    
    @FXML
    private ComboBox<String> comboAB;
    @FXML
    private TextArea textAreaInvestigationProjectName;
    @FXML
    private ComboBox<String> comboLGAC;
    @FXML
    private TextArea textAreaInvestigationLine;
    @FXML
    private TextField textFieldAproxDuration;
    @FXML
    private ComboBox<String> comboRecptionWorkModality;
    @FXML
    private TextArea textAreaReceptionWorkName;
    @FXML
    private TextArea textAreaRequisites;
    @FXML
    private ComboBox<String> comboDirectors;
    @FXML
    private ComboBox<String> comboCodirectors;
    @FXML
    private ComboBox<Integer> comboStudents;
    @FXML
    private TextArea textAreaInvestigationProjectDescription;
    @FXML
    private TextArea textAreaReceptionWorkDescription;
    @FXML
    private TextArea textAreaExpectedResults;
    @FXML
    private TextArea textAreaRecommendedBibliography;
    
    
    public void initialize() throws SQLException {
        fillLgacCombo();
        fillProfessorsCombos();
        fillReceptionWorkModalityCombo();
        fillStudentsCombo();
        fillABcombo();
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
    
    public void fillReceptionWorkModalityCombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> rwModalities = FXCollections.observableArrayList();
        List<String> rwModalitiesList = new ArrayList<>(projectDAO.getRWModalitiesList());
        rwModalities.addAll(rwModalitiesList);
        
        comboRecptionWorkModality.setItems(rwModalities);
    
    }
    
    public void fillStudentsCombo() {
        ObservableList<Integer> numberOfStudents = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        comboStudents.setItems(numberOfStudents);
    }
    
    public void fillABcombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> academicBodyID = FXCollections.observableArrayList();
        List<String> academicBodyIDList = new ArrayList<>(projectDAO.getAcademicBodyIDs());
        academicBodyID.addAll(academicBodyIDList);
        
        comboAB.setItems(academicBodyID);
    }
    
    public boolean emptyFields() {
        return comboAB.getValue() == null
                || comboLGAC.getValue() == null
                || textAreaInvestigationLine.getText().isBlank()
                || textFieldAproxDuration.getText().isBlank()
                || comboRecptionWorkModality.getValue() == null
                || textAreaReceptionWorkName.getText().isBlank()
                || textAreaRequisites.getText().isBlank()
                || comboDirectors.getValue() == null
                || comboCodirectors.getValue() == null
                || comboStudents.getValue() == null
                || textAreaInvestigationProjectDescription.getText().isBlank()
                || textAreaReceptionWorkDescription.getText().isBlank()
                || textAreaExpectedResults.getText().isBlank()
                || textAreaRecommendedBibliography.getText().isBlank();
    }
    
    public boolean overSizeData() {
        return textAreaInvestigationProjectName.getText().length() > 200
                || textAreaInvestigationLine.getText().length() > 300
                || textFieldAproxDuration.getText().length() > 10
                || textAreaReceptionWorkName.getText().length() > 200
                || textAreaRequisites.getText().length() > 500
                || textAreaInvestigationProjectDescription.getText().length() > 5000
                || textAreaReceptionWorkDescription.getText().length() > 5000
                || textAreaExpectedResults.getText().length() > 1000
                || textAreaRecommendedBibliography.getText().length() > 6000;
    }
    
    public void clear() {
        comboAB.setValue(comboAB.getPromptText());
        textAreaInvestigationProjectName.clear();
        comboLGAC.setValue(comboLGAC.getPromptText());
        textAreaInvestigationLine.clear();
        textFieldAproxDuration.clear();
        comboRecptionWorkModality.setValue(comboRecptionWorkModality.getPromptText());
        textAreaReceptionWorkName.clear();
        textAreaRequisites.clear();
        comboDirectors.setValue(comboDirectors.getPromptText());
        comboCodirectors.setValue(comboCodirectors.getPromptText());
        comboStudents.setValue(null);
        textAreaInvestigationProjectDescription.clear();
        textAreaReceptionWorkDescription.clear();
        textAreaExpectedResults.clear();
        textAreaRecommendedBibliography.clear();
    }
    
    public boolean validFields() {
        boolean flag;
        
        if (emptyFields()) {
            DialogGenerator.getDialog(new AlertMessage("Se deben llenar todos los campos.", AlertStatus.WARNING));
            flag = false;
        } else {
            if (overSizeData()) {
                DialogGenerator.getDialog(new AlertMessage("La información sobrepasa el límite de caracteres.", AlertStatus.WARNING));
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;
    }
    
    public void register() {
        if (validFields()){
            try {
                registerProject();
                DialogGenerator.getDialog(new AlertMessage("Se registró el anteproyecto exitosamente", AlertStatus.SUCCESS));
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("Error al registrar el anteproyecto, inténtelo más tarde", AlertStatus.ERROR));
                clear();
                sqlException.printStackTrace();
            }
        }
    }
    
    public void registerProject() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        Project project = new Project();
        
        project.setAcademicBodyId(comboAB.getSelectionModel().getSelectedItem());
        project.setInvestigationProjectName(textAreaInvestigationProjectName.getText());
        project.setLGAC_Id(comboLGAC.getSelectionModel().getSelectedIndex() + 1);
        project.setInvestigationLine(textAreaInvestigationLine.getText());
        project.setApproximateDuration(textFieldAproxDuration.getText());
        project.setModalityId(comboRecptionWorkModality.getSelectionModel().getSelectedIndex() + 1);
        project.setReceptionWorkName(textAreaReceptionWorkName.getText());
        project.setRequisites(textAreaRequisites.getText());
        project.setDirectorName(comboDirectors.getSelectionModel().getSelectedItem());
        project.setCodirectorName(comboCodirectors.getSelectionModel().getSelectedItem());
        project.setStudentsParticipating(comboStudents.getSelectionModel().getSelectedItem());
        project.setInvestigationProjectDescription(textAreaInvestigationProjectDescription.getText());
        project.setReceptionWorkDescription(textAreaReceptionWorkDescription.getText());
        project.setExpectedResults(textAreaExpectedResults.getText());
        project.setRecommendedBibliography(textAreaRecommendedBibliography.getText());
        
        projectDAO.addProject(project);
        projectDAO.setDirectorIDtoProject(project);
        projectDAO.setCodirectorIDtoProject(project);
    }
}
