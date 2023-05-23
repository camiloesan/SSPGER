package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentEvidencesController implements IStudentNavigationBar {
    @FXML
    TableView<Advancement> tableViewAdvancement;
    @FXML
    private TableView<Evidence> tableViewEvidence;

    @FXML
    private void initialize() {
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
        try {
            fillTableViewEvidence();
            fillTableViewAdvancement();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo conectar con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            sqlException.printStackTrace();
        }
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
    public void redirectToRequest() {

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
            TransferEvidence.setEvidenceName(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceTitle());
            MainStage.changeView("modifyevidence-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    public void redirectToViewEvidenceDetails() throws IOException {
        if (evidenceIsSelect()) {
            TransferEvidence.setEvidenceId(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceId());
            MainStage.changeView("viewevidencedetails-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    private void fillTableViewEvidence() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidence.getItems().clear();
        tableViewEvidence.getItems().addAll(evidenceDAO
                .getEvidenceListByStudent(LoginController.sessionDetails.getId()));
    }

    @FXML
    private void fillTableViewAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        tableViewAdvancement.getItems().clear();
        tableViewAdvancement.getItems().addAll(advancementDAO
                .getAdvancementByStudentID(LoginController.sessionDetails.getId()));
    }

    private boolean advancementIsSelected() {
        boolean verification;
        if (tableViewAdvancement.getSelectionModel().getSelectedItem() == null) {
            DialogGenerator.getDialog(new AlertMessage("Selecciona un avance para agregarle una evidencia", AlertStatus.WARNING));
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
            int evidenceID = tableViewEvidence.getSelectionModel().getSelectedItem().getEvidenceId();
            try {
                evidenceDAO.deleteEvidenceByID(evidenceID);
            } catch (SQLException deleteException) {
                deleteException.printStackTrace();
            }
        }
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
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar la evidencia?");
        return response.get() == DialogGenerator.BUTTON_YES;
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
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
