package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentEvidencesController implements IStudentNavigationBar {
    @FXML
    private TableView<Advancement> tableViewAdvancement;
    @FXML
    private TableView<Evidence> tableViewEvidence;
    @FXML
    private Label labelUsername;
    private static final Logger logger = Logger.getLogger(StudentEvidencesController.class);

    @FXML
    private void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        TableColumn<Evidence, String> titleEvidence = new TableColumn<>("Título");
        titleEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        TableColumn<Evidence, String> statusEvidence = new TableColumn<>("Estado");
        statusEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceStatus"));
        tableViewEvidence.getColumns().addAll(titleEvidence, statusEvidence);

        TableColumn<Advancement, String> nameAdvancement = new TableColumn<>("Avance");
        nameAdvancement.setCellValueFactory(new PropertyValueFactory<>("advancementName"));
        TableColumn<Advancement, String> starAdvancement = new TableColumn<>("Fecha de inicio");
        starAdvancement.setCellValueFactory(new PropertyValueFactory<>("advancementStartDate"));
        TableColumn<Advancement, String> finishAdvancement = new TableColumn<>("Fecha de cierre");
        finishAdvancement.setCellValueFactory(new PropertyValueFactory<>("advancementDeadline"));
        tableViewAdvancement.getColumns().addAll(nameAdvancement, starAdvancement, finishAdvancement);
        fillTables();

        if (SessionDetails.getInstance().getUserType().equals("Estudiante")) {

        }
    }

    private ObservableList<File> getFiles() {
        File folder = new File(getEvidenceDirectory().getAbsolutePath());
        File[] files = folder.listFiles();

        return FXCollections.observableArrayList(files);
    }

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() throws IOException {
        MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 +
                MainStage.HEIGHT_OFFSET);
    }

    @FXML
    public void redirectToAddEvidence() throws IOException {
        if (advancementIsSelected()) {
            TransferAdvancement.setAdvancementID(tableViewAdvancement
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementID());
            TransferAdvancement.setAdvancementName(tableViewAdvancement
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementName());
            MainStage.changeView("addevidence-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    public void redirectToModifyEvidence() throws IOException {
        if (evidenceIsSelect()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            TransferAdvancement.setAdvancementID(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementId());
            TransferEvidence.setEvidenceName(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceTitle());
            MainStage.changeView("modifyevidence-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    private void redirectToFiles() throws IOException {
        if (evidenceIsSelect()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            TransferAdvancement.setAdvancementID(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementId());
            MainStage.changeView("evidencefiles-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    public void redirectToViewEvidenceDetails() throws IOException {
        if (evidenceIsSelect()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            TransferAdvancement.setAdvancementID(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getAdvancementId());
            MainStage.changeView(
                    "viewevidencedetails-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }
    
    private void fillTables() {
        try {
            fillTableViewEvidence();
            fillTableViewAdvancement();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar la información.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    @FXML
    private void refreshTableViewEvidence() {
        try {
            fillTableViewEvidence();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar las evidencias registradas.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    private void fillTableViewEvidence() throws SQLException{
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidence.getItems().clear();
        tableViewEvidence.getItems().addAll(evidenceDAO
                .getEvidenceListByStudent(SessionDetails.getInstance().getId()));
    }

    @FXML
    private void refreshTableViewAdvancements() {
        try {
            fillTableViewAdvancement();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar los avances programados.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    private void fillTableViewAdvancement() throws SQLException{
        AdvancementDAO advancementDAO = new AdvancementDAO();
        tableViewAdvancement.getItems().clear();
        tableViewAdvancement.getItems().addAll(advancementDAO
                .getAdvancementByStudentID(SessionDetails.getInstance().getId()));
    }

    private boolean advancementIsSelected() {
        boolean verification;
        if (tableViewAdvancement.getSelectionModel().getSelectedItem() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Selecciona un avance para agregarle una evidencia", AlertStatus.WARNING));
            verification = false;
        } else {
            verification = true;
        }
        return verification;
    }

    @FXML
    private void deleteEvidence() {
        if (evidenceIsSelect() && confirmedDelete() ) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            int result = 0;
            int evidenceID = tableViewEvidence.getSelectionModel().getSelectedItem().getEvidenceId();

            if (!getFiles().isEmpty()) {
                for (File fileToDelete : getFiles()) {
                    fileToDelete.delete();
                }
            }

            try {
                result = evidenceDAO.deleteEvidenceByID(evidenceID);
            } catch (SQLException deleteException) {
                DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo " +
                        "eliminar la evidencia.", AlertStatus.ERROR));
                logger.error(deleteException);
            }

            if (result == 1) {
                DialogGenerator.getDialog(new AlertMessage("Evidencia eliminada", AlertStatus.SUCCESS));
                refreshTableViewEvidence();
            }
        }
    }

    private File getEvidenceDirectory() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String projectName = null;
        String advancementName = null;
        try {
            projectName = evidenceDAO.getProjectNameByEvidenceID(
                    tableViewEvidence.getSelectionModel().getSelectedItem().getEvidenceId());
            advancementName = evidenceDAO.getAdvancementNameByStudentID(
                    SessionDetails.getInstance().getId(),
                    tableViewEvidence.getSelectionModel().getSelectedItem().getAdvancementId());
        } catch(SQLException evidenceDAOException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar información para la carpeta de evidencias", AlertStatus.ERROR));
            logger.error(evidenceDAOException);
        }
        return new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectName+"/"
                +advancementName+"/"
                +SessionDetails.getInstance().getId());
    }

    private boolean evidenceIsSelect() {
        boolean result;
        if (tableViewEvidence.getSelectionModel().getSelectedItem() == null){
            DialogGenerator.getDialog(new AlertMessage("Selecciona una evidencia de la tabla",
                    AlertStatus.WARNING));
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    private boolean confirmedDelete() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea eliminar la evidencia?");
        return response.get() == DialogGenerator.BUTTON_YES;
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
