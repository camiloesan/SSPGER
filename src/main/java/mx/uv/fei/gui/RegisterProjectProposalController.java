package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
    private static final int MAX_INVESTIGATION_NAME_SIZE = 200;
    private static final int MAX_RECEPTION_WORK_NAME_SIZE = 200;
    private static final int MAX_INVESTIGATION_LINE_SIZE = 300;
    private static final int MAX_REQUISITES_SIZE = 500;
    private static final int MAX_RECEPTION_WORK_DESCRIPTION_SIZE = 5000;
    private static final int MAX_INVESTIGATION_DESCRIPTION_SIZE = 5000;
    private static final int MAX_EXPECTED_RESULTS_SIZE = 1000;
    private static final int MAX_RECOMMENDED_BIBLIOGRAPHY_SIZE = 6000;
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
                || comboDuration.getValue() == null
                || comboRecptionWorkModality.getValue() == null
                || textAreaReceptionWorkName.getText().isBlank()
                || textAreaRequisites.getText().isBlank()
                || comboDirectors.getValue() == null
                || comboCodirectors.getValue() == null
                || comboStudents.getValue() == null
                || textAreaReceptionWorkDescription.getText().isBlank()
                || textAreaExpectedResults.getText().isBlank()
                || textAreaRecommendedBibliography.getText().isBlank();
    }
    
    private ArrayList<String> emptyFieldsList = new ArrayList<>();
    public void fillEmptyFieldsList() {
        if (emptyFields()) {
            if (comboAB.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar un Cuerpo Académico");
            } if (comboLGAC.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar un LGAC");
            } if (comboDuration.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una duración");
            } if (comboRecptionWorkModality.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una Modalidad de Trabajo Recepcional");
            } if (textAreaReceptionWorkName.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar un Nombre de Trabajo Recepcional");
            } if (textAreaRequisites.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar los Requisitos");
            } if (comboDirectors.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar un Director");
            } if (comboCodirectors.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar un Codirector");
            } if (comboStudents.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una cantidad de estudiantes participantes");
            } if (textAreaReceptionWorkDescription.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar la descripción del Trabajo Recepcional");
            } if (textAreaExpectedResults.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar los resultados esperados");
            } if (textAreaRecommendedBibliography.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar la bibliografía recomendada");
            }
        }
    }
    
    private boolean overSizeData() {
        return textAreaInvestigationProjectName.getText().length() > MAX_INVESTIGATION_NAME_SIZE
                || textAreaInvestigationLine.getText().length() > MAX_INVESTIGATION_LINE_SIZE
                || textAreaReceptionWorkName.getText().length() > MAX_RECEPTION_WORK_NAME_SIZE
                || textAreaRequisites.getText().length() > MAX_REQUISITES_SIZE
                || textAreaInvestigationProjectDescription.getText().length() > MAX_INVESTIGATION_DESCRIPTION_SIZE
                || textAreaReceptionWorkDescription.getText().length() > MAX_RECEPTION_WORK_DESCRIPTION_SIZE
                || textAreaExpectedResults.getText().length() > MAX_EXPECTED_RESULTS_SIZE
                || textAreaRecommendedBibliography.getText().length() > MAX_RECOMMENDED_BIBLIOGRAPHY_SIZE;
    }
    
    private ArrayList<String> overSizeFieldsList = new ArrayList<>();
    private void fillOverSizeDataList() {
        if (overSizeData()) {
            if (textAreaInvestigationProjectName.getText().length() > MAX_INVESTIGATION_NAME_SIZE) {
                overSizeFieldsList.add("• El nombre de Proyecto de Investigación excede el límite de caracteres: " + MAX_INVESTIGATION_NAME_SIZE);
            } if (textAreaInvestigationLine.getText().length() > MAX_INVESTIGATION_LINE_SIZE) {
                overSizeFieldsList.add("• El nombre de la Línea de Investigación excede el límite de caracteres: " + MAX_INVESTIGATION_LINE_SIZE);
            } if (textAreaReceptionWorkName.getText().length() > MAX_RECEPTION_WORK_NAME_SIZE) {
                overSizeFieldsList.add("• El nombre del trabajo Recepcional excede el límite de caracteres: " + MAX_RECEPTION_WORK_NAME_SIZE);
            } if (textAreaRequisites.getText().length() > MAX_REQUISITES_SIZE) {
                overSizeFieldsList.add("• Los requisitos exceden el límite de caracteres: " + MAX_REQUISITES_SIZE);
            } if (textAreaInvestigationProjectDescription.getText().length() > MAX_INVESTIGATION_DESCRIPTION_SIZE) {
                overSizeFieldsList.add("• La descripción del Proyecto de Investigación excede el límite de caracteres: " + MAX_INVESTIGATION_DESCRIPTION_SIZE);
            } if (textAreaReceptionWorkDescription.getText().length() > MAX_RECEPTION_WORK_DESCRIPTION_SIZE) {
                overSizeFieldsList.add("• La descripción del Trabajo Recepcional excede el límite de caracteres: " + MAX_RECEPTION_WORK_DESCRIPTION_SIZE);
            } if (textAreaExpectedResults.getText().length() > MAX_EXPECTED_RESULTS_SIZE) {
                overSizeFieldsList.add("• Los Resultados Esperados exceden el límite de caracteres: " + MAX_EXPECTED_RESULTS_SIZE);
            } if (textAreaRecommendedBibliography.getText().length() > MAX_RECOMMENDED_BIBLIOGRAPHY_SIZE) {
                overSizeFieldsList.add("• La Bibliografía Recomendada excede el límite de caracteres: " + MAX_RECOMMENDED_BIBLIOGRAPHY_SIZE);
            }
        }
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
    
    private String buildFieldsAlert(ArrayList<String> fieldsList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem : fieldsList) {
            stringBuilder.append(elem).append("\n");
        }
        return stringBuilder.toString();
    }
    
    private boolean validFields() {
        boolean flag;
        if (!projectAlreadyRegistered()) {
            if (emptyFields()) {
                emptyFieldsList.clear();
                fillEmptyFieldsList();
                String emptyFields = buildFieldsAlert(emptyFieldsList);
                DialogGenerator.getDialog(new AlertMessage("Debe ingresar toda la información: \n" + emptyFields, AlertStatus.WARNING));
                flag = false;
            } else if (overSizeData()) {
                overSizeFieldsList.clear();
                fillOverSizeDataList();
                String overSizeFields = buildFieldsAlert(overSizeFieldsList);
                DialogGenerator.getDialog(new AlertMessage("La información excede el límite de caracteres: \n" + overSizeFields, AlertStatus.WARNING));
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
