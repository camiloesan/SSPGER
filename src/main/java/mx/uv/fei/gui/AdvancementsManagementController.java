package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

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
    @FXML
    private HBox hboxLogOutLabel;
    @FXML
    private ListView<String> listViewAdvancements;
    private int professorId;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_DESCRIPTION = 800;
    
    @FXML
    private void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        ProfessorDAO professorDAO = new ProfessorDAO();
        professorId = professorDAO.getProfessorIdByUsername(LoginController.sessionDetails.getUsername());
        fillComboBoxProjectToAssign();
        fillComboBoxNewProjectToAssign();
        fillListViewAdvancements();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    @FXML
    private void deleteAdvancementButtonAction() {
        if (listViewAdvancements.getSelectionModel().getSelectedItem() != null) {
            String advancementName = listViewAdvancements.getSelectionModel().getSelectedItem();
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar el avance \"" + advancementName + "\"?");
            if (response.get() == DialogGenerator.BUTTON_YES) {
                deleteAdvancement(listViewAdvancements.getSelectionModel().getSelectedItem());
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar un avance para eliminarlo", AlertStatus.WARNING));
        }
    }

    private void deleteAdvancement(String advancementName) {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            advancementDAO.deleteAdvancementByName(advancementName);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo eliminar el avance, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void scheduleAdvancementButtonAction() {
        if (areScheduleAdvancementFieldsValid()) {
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea añadir el avance?");
            if (response.get() == DialogGenerator.BUTTON_YES) {
                try {
                    scheduleAdvancement();
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("No se pudo añadir el avance, inténtelo más tarde", AlertStatus.ERROR));
                }
            }
        }
    }

    private void scheduleAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(advancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(advancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(advancementDeadline.getValue())));
        advancement.setProjectId(projectDAO.getProjectIdByName(comboProjectToAssign.getValue()));
        advancement.setAdvancementDescription(advancementDescription.getText());
        advancementDAO.addAdvancement(advancement);
    }

    private boolean areScheduleAdvancementFieldsValid() {
        if (advancementName.getText().isBlank()
                || advancementStartDate.getValue() == null
                || advancementDeadline.getValue() == null
                || comboProjectToAssign.getValue().isBlank()
                || advancementDescription.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (advancementName.getText().length() >= MAX_LENGTH_NAME
                || advancementDescription.getText().length() >= MAX_LENGTH_DESCRIPTION) {
            DialogGenerator.getDialog(new AlertMessage("El límite de caracteres fue sobrepasado, inténtalo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void modifyAdvancementButtonAction() {
        if (areModifyAdvancementFieldsValid()) {
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea modificar el avance?");
            if (response.get() == DialogGenerator.BUTTON_YES) {
                try {
                    modifyAdvancement();
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("Ocurrió un error, no se pudo modificar el avance", AlertStatus.ERROR));
                }
            }
        }
    }

    private boolean areModifyAdvancementFieldsValid() {
        if (advancementToModify.getText().isBlank()
                || newAdvancementName.getText().isBlank()
                || newAdvancementStartDate.getValue() == null
                || newAdvancementDeadline.getValue() == null
                || newAdvancementDescription.getText().isBlank()
                || comboNewProjectToAssign.getValue().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (newAdvancementName.getText().length() >= MAX_LENGTH_NAME
                || newAdvancementDescription.getText().length() >= MAX_LENGTH_DESCRIPTION) {
            DialogGenerator.getDialog(new AlertMessage("El límite de caracteres fue sobrepasado, inténtalo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private void modifyAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(newAdvancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(newAdvancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(newAdvancementDeadline.getValue())));
        advancement.setProjectId(projectDAO.getProjectIdByName(comboNewProjectToAssign.getValue()));
        advancement.setAdvancementDescription(newAdvancementDescription.getText());
        advancementDAO.modifyAdvancementByName(advancementToModify.getText(), advancement);
    }

    private void fillComboBoxProjectToAssign() {
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            comboProjectToAssign.setItems((FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId))));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("Hubo un problema al conectarse con la base de datos", AlertStatus.ERROR));
        }
    }

    private void fillComboBoxNewProjectToAssign() {
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            comboNewProjectToAssign.setItems(FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId)));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("Hubo un problema al conectarse con la base de datos", AlertStatus.ERROR));
        }
    }
    
    public void fillListViewAdvancements() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProfessorDAO professorDAO = new ProfessorDAO();
        listViewAdvancements.getItems().clear();
        professorId = professorDAO.getProfessorIdByUsername(LoginController.sessionDetails.getUsername());
        
        List<Advancement> advancementList = new ArrayList<>(advancementDAO.getListAdvancementName(professorId));
        advancementList.forEach(element -> listViewAdvancements.getItems().add(element.getAdvancementName()));
    }

    @Override
    public void redirectToAdvancementManagement() {

    }

    @Override
    public void redirectToProjectManagement() throws IOException {
        MainStage.changeView("timeline-view.fxml", 700, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
