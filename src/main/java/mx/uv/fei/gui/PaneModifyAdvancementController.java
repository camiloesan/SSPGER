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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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
        fillComboBoxNewProjectToAssign();
        formatDatePickers();
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

    private void fillComboBoxNewProjectToAssign() {
        ProjectDAO projectDAO = new ProjectDAO();
        int professorId = Integer.parseInt(LoginController.sessionDetails.getId());
        try {
            comboNewProjectToAssign.setItems(FXCollections.observableList(
                    projectDAO.getProjectNamesByIdDirector(professorId)));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Hubo un problema al conectarse con la base de datos", AlertStatus.ERROR));
            logger.error(sqlException);
        }
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
                    clearFields();
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

    private void clearFields() {
        newAdvancementName.clear();
        newAdvancementStartDate.setValue(null);
        newAdvancementDeadline.setValue(null);
        comboNewProjectToAssign.setValue(null);
        newAdvancementDescription.clear();
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

    private boolean areModifyAdvancementFieldsValid() {
        if (newAdvancementName.getText().isBlank()
                || newAdvancementStartDate.getValue() == null
                || newAdvancementDeadline.getValue() == null
                || newAdvancementDescription.getText().isBlank()
                || comboNewProjectToAssign.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (newAdvancementName.getText().length() >= MAX_LENGTH_NAME) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El nombre del avance es demasiado largo. (máx. " + MAX_LENGTH_NAME + ")",
                    AlertStatus.WARNING
            ));
            return false;
        } else if (newAdvancementDescription.getText().length() >= MAX_LENGTH_DESCRIPTION) {
            DialogGenerator.getDialog(new AlertMessage(
                    "El límite de caracteres en la descripción fue sobrepasado, (máx. " + MAX_LENGTH_DESCRIPTION + ").",
                    AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }
}
