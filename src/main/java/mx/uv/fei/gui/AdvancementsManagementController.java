package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AdvancementsManagementController implements IProfessorNavigationBar {
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
    private ListView<String> listViewAdvancements;
    @FXML
    private Tab tabViewAdvancements;
    private int professorId;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_DESCRIPTION = 800;
    
    @FXML
    private void initialize() throws SQLException {

        SessionDetails sd = SessionDetails.getInstance("xxxx", "xxxx", "abcd");
        System.out.println("id: " + System.identityHashCode(sd) + " nombre: " + sd.getUsername());

        labelUsername.setText(SessionDetails.getInstance().getUsername());
        ProfessorDAO professorDAO = new ProfessorDAO();
        professorId = professorDAO.getProfessorIdByUsername(LoginController.sessionDetails.getUsername());
        fillComboBoxProjectToAssign();
        formatDatePickers();
        fillListViewAdvancements();
    }

    private void formatDatePickers() {
        advancementStartDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });

        advancementDeadline.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });
    }

    @FXML
    private void deleteAdvancementButtonAction() {
        if (isItemSelected()) {
            String advancementName = listViewAdvancements.getSelectionModel().getSelectedItem();
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar el avance \"" + advancementName + "\"?");
            if (response.get() == DialogGenerator.BUTTON_YES) {
                deleteAdvancement(listViewAdvancements.getSelectionModel().getSelectedItem());
                try {
                    fillListViewAdvancements();
                } catch (SQLException sqlException) {
                    DialogGenerator.getDialog(new AlertMessage("No se pudo actualizar la tabla", AlertStatus.WARNING));
                }
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar un avance para eliminarlo", AlertStatus.WARNING));
        }
    }

    private boolean isItemSelected() {
        return listViewAdvancements.getSelectionModel().getSelectedItem() != null;
    }

    private void deleteAdvancement(String advancementName) {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            advancementDAO.deleteAdvancementByName(advancementName);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo eliminar el avance, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            sqlException.printStackTrace();
        }
    }

    @FXML
    private void scheduleAdvancementButtonAction() {
        if (areScheduleAdvancementFieldsValid()) {
            try {
                scheduleAdvancement();
                DialogGenerator.getDialog(new AlertMessage("Se ha programado el avance", AlertStatus.SUCCESS));
            } catch (SQLWarning sqlWarning) {
                DialogGenerator.getDialog(new AlertMessage("El avance ya esta guardado", AlertStatus.ERROR));
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No se pudo añadir el avance, inténtelo más tarde", AlertStatus.ERROR));
            }
            try {
                fillListViewAdvancements();
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No se pudo actualizar la tabla, inténtelo más tarde", AlertStatus.WARNING));
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
        advancement.setProjectId(projectDAO.getProjectIDByTitle(comboProjectToAssign.getValue()));
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
    private void openModifyAdvancementPane() throws IOException {
        if (listViewAdvancements.getSelectionModel().getSelectedItem() != null) {
            String advancementName = listViewAdvancements.getSelectionModel().getSelectedItem();
            TransferAdvancement.setAdvancementName(advancementName);
            Parent modifyVbox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("panemodifyadvancement-view.fxml")));
            tabViewAdvancements.setContent(modifyVbox);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un avance para modificarlo.", AlertStatus.WARNING));
        }
    }

    private void fillComboBoxProjectToAssign() {
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            comboProjectToAssign.setItems((FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId))));
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
    
    public void openAdvancementDetails() throws IOException {
        if (listViewAdvancements.getSelectionModel().getSelectedItem() != null) {
            String advancementName = listViewAdvancements.getSelectionModel().getSelectedItem();
            TransferAdvancement.setAdvancementName(advancementName);
            
            Parent detailsVbox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("paneadvancementdetails-view.fxml")));
            
            tabViewAdvancements.setContent(detailsVbox);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Selecciones un avance para ver los detalles.", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        try {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
