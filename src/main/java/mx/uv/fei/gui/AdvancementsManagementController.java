package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.Advancement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdvancementsManagementController implements IProfessorNavigationBar {
    @FXML
    private TextField advancementToModify;
    @FXML
    private DatePicker newAdvancementDeadline;
    @FXML
    private TextArea newAdvancementDescription;
    @FXML
    private TextField newAdvancementName;
    @FXML
    private DatePicker newAdvancementStartDate;
    @FXML
    private ComboBox<String> comboNewProjectToAssign;
    @FXML
    private DatePicker advancementDeadline;
    @FXML
    private TextArea advancementDescription;
    @FXML
    private TextField advancementName;
    @FXML
    private DatePicker advancementStartDate;
    @FXML
    private ComboBox<String> comboProjectToAssign;
    @FXML
    private Label labelUsername;
    private int professorId;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_DESCRIPTION = 800;

    @FXML
    private void scheduleAdvancementButtonAction() {
        if (areScheduleAdvancementFieldsValid()) {
            try {
                scheduleAdvancement();
            } catch (SQLException sqlException) {
                //alert
            }
        } else {
            //alert: todos los campos deben estar llenos
        }
    }

    private void scheduleAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(advancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(advancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(advancementDeadline.getValue())));
        advancement.setProjectId(advancementDAO.getProjectIdByName(comboProjectToAssign.getValue()));
        advancement.setProfessorId(professorId);
        advancement.setAdvancementDescription(advancementDescription.getText());
        advancementDAO.addAdvancement(advancement);
    }

    private boolean areScheduleAdvancementFieldsValid() {
        if (advancementName.getText().isBlank()
                || advancementStartDate.getValue().toString().isBlank()
                || advancementDeadline.getValue().toString().isBlank()
                || comboProjectToAssign.getValue().isBlank()
                || advancementDescription.getText().isBlank()) {
            //alert all fields must be filled
            return false;
        } else if (advancementName.getText().length() >= MAX_LENGTH_NAME
                || advancementDescription.getText().length() >= MAX_LENGTH_DESCRIPTION) {
            //alert el limite de caracteres fue sobrepasado
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void modifyAdvancementButtonAction() {
        if (areModifyAdvancementFieldsValid()) {
            try {
                modifyAdvancement();
            } catch (SQLException sqlException) {
                //alert error bd
            }
        }
    }

    private boolean areModifyAdvancementFieldsValid() {
        if (advancementToModify.getText().isBlank()
                || newAdvancementName.getText().isBlank()
                || newAdvancementStartDate.getValue().toString().isBlank()
                || newAdvancementDeadline.getValue().toString().isBlank()
                || newAdvancementDescription.getText().isBlank()
                || comboNewProjectToAssign.getValue().isBlank()) {
            //alert
            return false;
        } else if (newAdvancementName.getText().length() >= MAX_LENGTH_NAME
                || newAdvancementDescription.getText().length() >= MAX_LENGTH_DESCRIPTION) {
            //alert
            return false;
        } else {
            return true;
        }
    }

    private void modifyAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(newAdvancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(newAdvancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(newAdvancementDeadline.getValue())));
        advancement.setProjectId(advancementDAO.getProjectIdByName(comboNewProjectToAssign.getValue()));
        advancement.setProfessorId(professorId);
        advancement.setAdvancementDescription(newAdvancementDescription.getText());
        advancementDAO.modifyAdvancementByName(advancementToModify.getText(), advancement);
    }

    private void fillComboBoxProjectToAssign() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        comboProjectToAssign.setItems((FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId))));
    }

    private void fillComboBoxNewProjectToAssign() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        comboNewProjectToAssign.setItems(FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId)));
    }

    @FXML
    private void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        AdvancementDAO advancementDAO = new AdvancementDAO();
        professorId = advancementDAO.getProfessorIdByUsername(LoginController.sessionDetails.getUsername());
        fillComboBoxProjectToAssign();
        fillComboBoxNewProjectToAssign();
    }

    @Override
    public void redirectToAdvancementManagement() {

    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
