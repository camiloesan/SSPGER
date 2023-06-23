package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.UnaryOperator;

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
    private TableView<Advancement> tableViewAdvancements;
    @FXML
    private Tab tabViewAdvancements;
    @FXML
    private Label labelRemainingChars;
    private int professorId;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_DESCRIPTION = 800;
    private static final Logger logger = Logger.getLogger(AdvancementsManagementController.class);
    
    @FXML
    private void initialize() {
        TableColumn<Advancement, String> advancementNameColumn = new TableColumn<>("Nombre");
        advancementNameColumn.setCellValueFactory(new PropertyValueFactory<>("advancementName"));
        TableColumn<Advancement, String> startDateColumn = new TableColumn<>("Fecha Inicio");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("advancementStartDate"));
        TableColumn<Advancement, String> endDateColumn = new TableColumn<>("Fecha Fin");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("advancementDeadline"));

        tableViewAdvancements.getColumns().addAll(Arrays.asList(advancementNameColumn, startDateColumn, endDateColumn));

        labelRemainingChars.setText("Caracteres disponibles: " + MAX_LENGTH_DESCRIPTION);

        labelUsername.setText(SessionDetails.getInstance().getUsername());
        professorId = Integer.parseInt(SessionDetails.getInstance().getId());
        advancementDescription.setTextFormatter(new TextFormatter<>(createFilter()));
        formatDatePicker();
        showAdvancementData();
    }
    
    private void showAdvancementData() {
        try {
            fillComboBoxProjectToAssign();
            fillTableViewAdvancements();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo recuperar" +
                    " la información de los avances.", AlertStatus.ERROR));
        }
    }

    private void formatDatePicker() {
        advancementStartDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });
    }
    
    private void refreshTableViewAdvancements() {
        try {
            fillTableViewAdvancements();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo actualizar" +
                    " la tabla de avances.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
 
    @FXML
    private void deleteAdvancementButtonAction() {
        if (isItemSelected()) {
            int advancementId = tableViewAdvancements.getSelectionModel().getSelectedItem().getAdvancementID();
            String advancementName = tableViewAdvancements.getSelectionModel().getSelectedItem().getAdvancementName();
            Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                    "¿Está seguro que desea eliminar el avance \"" + advancementName + "\"?");
            if (response.orElse(null) == DialogGenerator.BUTTON_YES) {
                deleteAdvancement(advancementId);
                refreshTableViewAdvancements();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Debes seleccionar un avance para eliminarlo", AlertStatus.WARNING));
        }
    }

    private boolean isItemSelected() {
        return tableViewAdvancements.getSelectionModel().getSelectedItem() != null;
    }

    private void deleteAdvancement(int advancementId) {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            advancementDAO.deleteAdvancementById(advancementId);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo eliminar el avance.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    @FXML
    private void updateAvailableCharsOnDescriptionLabel() {
        int descriptionLength = advancementDescription.getLength();
        labelRemainingChars.setText("Caracteres disponibles: " + (MAX_LENGTH_DESCRIPTION - descriptionLength));
    }
    
    private UnaryOperator<TextFormatter.Change> createFilter() {
        return change -> {
            int newLength = change.getControlNewText().length();
            if (newLength <= MAX_LENGTH_DESCRIPTION) {
                return change;
            }
            return null;
        };
    }

    @FXML
    private void enableDatePickerDeadline() {
        advancementDeadline.setDisable(false);
        advancementDeadline.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = advancementStartDate.getValue();
                setDisable(empty || date.isBefore(startDate));
            }
        });
    }

    @FXML
    private void scheduleAdvancementButtonAction() {
        if (areScheduleAdvancementFieldsValid()) {
            try {
                Advancement advancement = createAdvancement();
                scheduleAdvancement(advancement);
                fillTableViewAdvancements();
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo " +
                        "programar el avance.", AlertStatus.ERROR));
                logger.error(sqlException);
            }
        }
    }
    
    private boolean emptyFields() {
        return advancementName.getText().isBlank()
                || advancementStartDate.getValue() == null
                || advancementDeadline.getValue() == null
                || comboProjectToAssign.getValue() == null
                || advancementDescription.getText().isBlank();
    }
    
    private ArrayList<String> emptyFieldsList = new ArrayList<>();
    private void fillEmptyFieldsList() {
        if (emptyFields()) {
            if (advancementName.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar el nombre del avance");
            } if (advancementStartDate.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una fecha de inicio");
            } if (advancementDeadline.getValue() == null) {
                emptyFieldsList.add("• Debe seleccionar una fecha límite");
            } if (comboProjectToAssign.getValue() == null) {
                emptyFieldsList.add("• Debe asignar el avance a un Proyecto");
            } if (advancementDescription.getText().isBlank()) {
                emptyFieldsList.add("• Debe ingresar la descripción del avance");
            }
        }
    }
    
    private boolean overSizeData() {
        return advancementName.getText().length() > MAX_LENGTH_NAME
                || advancementDescription.getText().length() > MAX_LENGTH_DESCRIPTION;
    }
    
    private ArrayList<String> overSizeFieldsList = new ArrayList<>();
    private void fillOverSizeFieldsList() {
        if (overSizeData()) {
            if (advancementName.getText().length() > MAX_LENGTH_NAME) {
                overSizeFieldsList.add("• El nombre del Avance excede el límite de caracteres: " + MAX_LENGTH_NAME);
            } if (advancementDescription.getText().length() > MAX_LENGTH_DESCRIPTION) {
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

    private boolean areScheduleAdvancementFieldsValid() {
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
        } else if (advancementDeadline.getValue().isBefore(advancementStartDate.getValue())) {
            DialogGenerator.getDialog(new AlertMessage(
                    "La Fecha de cierre tiene que ser despues de la de inicio ", AlertStatus.WARNING));
        } else {
            flag = true;
        }
        return flag;
    }

    private Advancement createAdvancement() throws SQLException{
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement = new Advancement();
        
        advancement.setProjectId(projectDAO.getProjectIDByTitle(comboProjectToAssign.getValue()));
        advancement.setAdvancementName(advancementName.getText());
        advancement.setAdvancementDescription(advancementDescription.getText());
        advancement.setAdvancementStartDate(String.valueOf(java.sql.Date.valueOf(advancementStartDate.getValue())));
        advancement.setAdvancementDeadline(String.valueOf(java.sql.Date.valueOf(advancementDeadline.getValue())));

        return advancement;
    }
    
    private void scheduleAdvancement(Advancement advancement) throws SQLException{
        AdvancementDAO advancementDAO = new AdvancementDAO();
        
        if (advancementDAO.addAdvancement(advancement) == 1) {
            DialogGenerator.getDialog(new AlertMessage("Se ha programado el avance", AlertStatus.SUCCESS));
        }
    }

    private void clearFields() {
        advancementName.clear();
        advancementStartDate.setValue(null);
        advancementDeadline.setValue(null);
        comboProjectToAssign.setValue(null);
        advancementDescription.clear();
    }

    @FXML
    private void openModifyAdvancementPane() throws IOException {
        if (tableViewAdvancements.getSelectionModel().getSelectedItem() != null) {
            String advancementName = tableViewAdvancements.getSelectionModel().getSelectedItem().getAdvancementName();
            TransferAdvancement.setAdvancementName(advancementName);
            int advancementId = tableViewAdvancements.getSelectionModel().getSelectedItem().getAdvancementID();
            System.out.println(advancementId);
            TransferAdvancement.setAdvancementID(advancementId);
            Parent modifyVbox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "panemodifyadvancement-view.fxml")));
            tabViewAdvancements.setContent(modifyVbox);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un avance para modificarlo.", AlertStatus.WARNING));
        }
    }

    private void fillComboBoxProjectToAssign() throws SQLException{
        ProjectDAO projectDAO = new ProjectDAO();
        comboProjectToAssign.setItems(FXCollections.observableList(projectDAO.getProjectNamesByIdDirector(professorId)));
    }
    
    public void fillTableViewAdvancements() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        tableViewAdvancements.getItems().clear();
        professorId = Integer.parseInt(SessionDetails.getInstance().getId());
        List<Advancement> advancementList;
       advancementList = new ArrayList<>(advancementDAO.getListAdvancementNamesByProfessorId(professorId));
       tableViewAdvancements.getItems().addAll(advancementList);
    }
    
    public void openAdvancementDetails() throws IOException {
        if (tableViewAdvancements.getSelectionModel().getSelectedItem() != null) {
            int advancementId = tableViewAdvancements.getSelectionModel().getSelectedItem().getAdvancementID();
            TransferAdvancement.setAdvancementID(advancementId);
            Parent detailsVbox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                    "paneadvancementdetails-view.fxml")));
            tabViewAdvancements.setContent(detailsVbox);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Selecciones un avance para ver los detalles.", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_REPRESENTATIVE)) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_PROFESSOR)){
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
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
