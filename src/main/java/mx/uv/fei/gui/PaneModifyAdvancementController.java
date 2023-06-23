package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.TransferAdvancement;
import mx.uv.fei.logic.SessionDetails;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class PaneModifyAdvancementController {
    @FXML
    private ComboBox<String> comboNewProjectToAssign;
    @FXML
    private DatePicker newAdvancementDeadline;
    @FXML
    private TextArea newAdvancementDescription;
    @FXML
    private TextField newAdvancementName;
    @FXML
    private DatePicker newAdvancementStartDate;
    @FXML
    private Label labelHeader;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_DESCRIPTION = 800;
    private static final Logger logger = Logger.getLogger(PaneModifyAdvancementController.class);

    @FXML
    private void initialize() {
        labelHeader.setText("Modificar evidencia [" + TransferAdvancement.getAdvancementName() + "]");
        formatDatePickers();
        showAdvancementDetails();
    }
    
    public void showAdvancementDetails() {
        try {
            fillComboBoxNewProjectToAssign();
            getAdvancementToModify();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo recuperar" +
                    " la información del avance.", AlertStatus.ERROR));
        }
    }
    
    private void getAdvancementToModify() throws SQLException{
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement = advancementDAO.getAdvancementDetailById(TransferAdvancement.getAdvancementID());
        newAdvancementName.setText(advancement.getAdvancementName());
        newAdvancementStartDate.setValue(LocalDate.parse(advancement.getAdvancementStartDate()));
        newAdvancementDeadline.setValue(LocalDate.parse(advancement.getAdvancementDeadline()));
        setAsignedProject();
        newAdvancementDescription.setText(advancement.getAdvancementDescription());
    }

    private void formatDatePickers() {
        newAdvancementStartDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });

        newAdvancementDeadline.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });
    }

    @FXML
    private void enableDatePickerDeadline() {
        newAdvancementDeadline.setDisable(false);
        newAdvancementDeadline.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = newAdvancementStartDate.getValue();
                setDisable(empty || date.isBefore(startDate));
            }
        });
    }
    
    private void setAsignedProject() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        String actualAssignedProject = projectDAO.getProjectNameByAdvancementID(TransferAdvancement.getAdvancementID());
        comboNewProjectToAssign.setValue(actualAssignedProject);
    }

    private void fillComboBoxNewProjectToAssign() throws SQLException{
        ProjectDAO projectDAO = new ProjectDAO();
        int professorId = Integer.parseInt(SessionDetails.getInstance().getId());
        comboNewProjectToAssign.setItems(FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId)));
    }

    @FXML
    void returnToAdvancementList() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }

    @FXML
    private void modifyAdvancementButtonAction() {
        if (areModifyAdvancementFieldsValid()) {
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                    "¿Está seguro que desea modificar el avance?");
            if (response.orElse(null) == DialogGenerator.BUTTON_YES) {
                try {
                    modifyAdvancement();
                    DialogGenerator.getDialog(new AlertMessage(
                            "Se modificó el avance exitosamente", AlertStatus.SUCCESS));
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "Ocurrió un error, no se pudo modificar el avance", AlertStatus.ERROR));
                    logger.error(sqlException);
                }
            }
        }
    }

    private void modifyAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(newAdvancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(newAdvancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(newAdvancementDeadline.getValue())));
        advancement.setProjectId(projectDAO.getProjectIDByTitle(comboNewProjectToAssign.getValue()));
        advancement.setAdvancementDescription(newAdvancementDescription.getText());
        advancementDAO.modifyAdvancementById(TransferAdvancement.getAdvancementID(), advancement);
    }
    
    private boolean emptyFields() {
        return newAdvancementName.getText().isBlank()
                || newAdvancementStartDate.getValue() == null
                || newAdvancementDeadline.getValue() == null
                || comboNewProjectToAssign.getValue() == null
                || newAdvancementDescription.getText().isBlank();
    }
    
    private ArrayList<String> emptyFieldsList = new ArrayList<>();
    private void fillEmptyFieldsList() {
        if (emptyFields()) {
            if (newAdvancementName.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar el nombre del avance");
            } if (newAdvancementStartDate.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una fecha de inicio");
            } if (newAdvancementDeadline.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una fecha límite");
            } if (comboNewProjectToAssign.getValue() == null) {
                emptyFieldsList.add("• Debe asignar el avance a un Proyecto");
            } if (newAdvancementDescription.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar la descripción del avance");
            }
        }
    }
    
    private boolean overSizeData() {
        return newAdvancementName.getText().length() > MAX_LENGTH_NAME
                || newAdvancementDescription.getText().length() > MAX_LENGTH_DESCRIPTION;
    }
    
    private ArrayList<String> overSizeFieldsList = new ArrayList<>();
    private void fillOverSizeFieldsList() {
        if (overSizeData()) {
            if (newAdvancementName.getText().length() > MAX_LENGTH_NAME) {
                overSizeFieldsList.add("• El nombre del Avance excede el límite de caracteres: " + MAX_LENGTH_NAME);
            } if (newAdvancementDescription.getText().length() > MAX_LENGTH_DESCRIPTION) {
                overSizeFieldsList.add("• La descripción del avance excede el límite de caracteres: " + MAX_LENGTH_DESCRIPTION);
            }
        }
    }
    
    private String buildFieldsAlert(ArrayList<String> fieldsList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String elem : fieldsList) {
            stringBuilder.append(elem).append("\n");
        }
        return stringBuilder.toString();
    }
    
    private boolean areModifyAdvancementFieldsValid() {
        boolean flag = false;
        if (emptyFields()) {
            emptyFieldsList.clear();
            fillEmptyFieldsList();
            String emptyFields = buildFieldsAlert(emptyFieldsList);
            DialogGenerator.getDialog(new AlertMessage(
                    "Debe ingresar toda la información: \n" + emptyFields, AlertStatus.WARNING));
        } else if (overSizeData()) {
            overSizeFieldsList.clear();
            fillOverSizeFieldsList();
            String overSizeFields = buildFieldsAlert(overSizeFieldsList);
            DialogGenerator.getDialog(new AlertMessage("La información excede el límite de caracteres: \n" +
                    overSizeFields, AlertStatus.WARNING));
        } else if (newAdvancementDeadline.getValue().isBefore(newAdvancementStartDate.getValue())){
            DialogGenerator.getDialog(new AlertMessage(
                    "La Fecha de cierre tiene que ser despues de la de inicio ", AlertStatus.WARNING));
        } else {
            flag = true;
        }
        return flag;
    }
}
