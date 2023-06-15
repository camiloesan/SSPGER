package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;

import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.SessionDetails;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RegisterProjectProposalController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ComboBox<String> comboAB;
    @FXML
    private TextArea textAreaInvestigationProjectName;
    @FXML
    private ComboBox<String> comboLGAC;
    @FXML
    private TextArea textAreaInvestigationLine;
    @FXML
    private ComboBox<String> comboDuration;
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
    @FXML
    private HBox hboxLogOutLabel;
    
    private static final Logger logger = Logger.getLogger(ProjectRequestsController.class);
    
    public void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillCombos();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    private void fillCombos() {
        try {
            fillLgacCombo();
            fillProfessorsCombos();
            fillReceptionWorkModalityCombo();
            fillAcademicBodycombo();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información necesaria para el registro.",AlertStatus.ERROR));
            logger.error(sqlException);
        }
        fillStudentsCombo();
        fillDurationCombo();
    }
    
    private void fillLgacCombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> lgacComboItems = FXCollections.observableArrayList();
        List<String> lgacList = new ArrayList<>(projectDAO.getLgacList());
        if (lgacList.isEmpty()) {
            DialogGenerator.getDialog(new AlertMessage("No hay LGAC registrados", AlertStatus.WARNING));
        }
        lgacComboItems.addAll(lgacList);
        comboLGAC.setItems(lgacComboItems);
    }
    
    private void fillProfessorsCombos() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        ObservableList<String> professorsNames = FXCollections.observableArrayList();
        List<String> professorsList = new ArrayList<>(professorDAO.getProfessorsNames());
        if (professorsList.isEmpty()) {
            DialogGenerator.getDialog(new AlertMessage("No hay profesores registrados", AlertStatus.WARNING));
        }
        professorsNames.addAll(professorsList);
        comboDirectors.setItems(professorsNames);
        comboCodirectors.setItems(professorsNames);
    }
    
    private void fillReceptionWorkModalityCombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> rwModalities = FXCollections.observableArrayList();
        List<String> rwModalitiesList = new ArrayList<>(projectDAO.getRWModalitiesList());
        if (rwModalitiesList.isEmpty()) {
            DialogGenerator.getDialog(new AlertMessage("No hay modalidades de trabajo recepcional registradas", AlertStatus.WARNING));
        }
        rwModalities.addAll(rwModalitiesList);
        comboRecptionWorkModality.setItems(rwModalities);
    
    }
    
    private void fillStudentsCombo() {
        ObservableList<Integer> numberOfStudents = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        comboStudents.setItems(numberOfStudents);
    }
    
    private void fillDurationCombo() {
        ObservableList<String> numberOfStudents = FXCollections.observableArrayList("6 meses", "12 meses", "18 meses");
        comboDuration.setItems(numberOfStudents);
    }
    
    private void fillAcademicBodycombo() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        ObservableList<String> academicBodyID = FXCollections.observableArrayList();
        List<String> academicBodyIDList = new ArrayList<>(projectDAO.getAcademicBodyIDs());
        academicBodyID.addAll(academicBodyIDList);
        comboAB.setItems(academicBodyID);
    }
    
    private boolean emptyFields() {
        return comboAB.getValue() == null
                || comboLGAC.getValue() == null
                || textAreaInvestigationLine.getText().isBlank()
                || comboDuration.getValue() == null
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
    
    private boolean overSizeData() {
        return textAreaInvestigationProjectName.getText().length() > 200
                || textAreaInvestigationLine.getText().length() > 300
                || textAreaReceptionWorkName.getText().length() > 200
                || textAreaRequisites.getText().length() > 500
                || textAreaInvestigationProjectDescription.getText().length() > 5000
                || textAreaReceptionWorkDescription.getText().length() > 5000
                || textAreaExpectedResults.getText().length() > 1000
                || textAreaRecommendedBibliography.getText().length() > 6000;
    }
    
    private boolean projectAlreadyRegistered() {
        boolean flag;
        
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            flag = projectDAO.isProjectRegistered(textAreaReceptionWorkName.getText());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog( new AlertMessage("Error al conectar con la base datos.",AlertStatus.ERROR));
            logger.error(sqlException);
            flag = false;
        }
        return flag;
    }
    
    @FXML
    private void clearFields() {
        comboAB.setValue(comboAB.getPromptText());
        textAreaInvestigationProjectName.clear();
        comboLGAC.setValue(comboLGAC.getPromptText());
        textAreaInvestigationLine.clear();
        comboDuration.setValue(comboDuration.getPromptText());
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
    
    private boolean validFields() {
        boolean flag;
        if (!projectAlreadyRegistered()) {
            if (emptyFields()) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Se deben llenar todos los campos.", AlertStatus.WARNING));
                flag = false;
            } else if (overSizeData()) {
                DialogGenerator.getDialog(new AlertMessage(
                        "La información sobrepasa el límite de caracteres.", AlertStatus.WARNING));
                flag = false;
            } else {
                flag = true;
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Ya se ha registrado un proyecto con ese título",AlertStatus.WARNING));
            flag = false;
        }
        return flag;
    }
    
    @FXML
    private void actionRegister() {
        if (validFields()){
            try {
                if (registerProject() == 1) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "Se registró el anteproyecto exitosamente", AlertStatus.SUCCESS));
                } else {
                    DialogGenerator.getDialog(new AlertMessage(
                            "Error al registrar el anteproyecto, inténtelo más tarde", AlertStatus.WARNING));
                }
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error al registrar el anteproyecto, inténtelo más tarde", AlertStatus.ERROR));
                clearFields();
                logger.error(sqlException);
            }
        }
    }
    
    private int registerProject() throws SQLException {
        int result;
        ProjectDAO projectDAO = new ProjectDAO();
        Project project = new Project();
        
        project.setAcademicBodyId(comboAB.getSelectionModel().getSelectedItem());
        project.setInvestigationProjectName(textAreaInvestigationProjectName.getText());
        project.setLGAC_Id(comboLGAC.getSelectionModel().getSelectedIndex() + 1);
        project.setInvestigationLine(textAreaInvestigationLine.getText());
        project.setApproximateDuration(comboDuration.getSelectionModel().getSelectedItem());
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
        
        if (projectDAO.addProject(project) == 1
        && projectDAO.setDirectorIDtoProject(project) == 1
        && projectDAO.setCodirectorIDtoProject(project) ==1 ) {
            result = 1;
        } else {
            result = 0;
        }
        
        return result;
    }
    
    @FXML
    private void returnToProjectManagement() throws IOException{
        redirectToProfessorProjectManagement();
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (Objects.equals(LoginController.sessionDetails.getUserType(), "RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (Objects.equals(LoginController.sessionDetails.getUserType(), "Profesor")){
            MainStage.changeView(
                    "professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
