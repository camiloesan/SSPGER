package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;

public class ModifyEvidenceController implements IStudentNavigationBar {
    @FXML
    Label labelAdvancementTitle;
    @FXML
    Label labelFileTitle;
    @FXML
    Label labelUsername;
    @FXML
    TextField textFieldEvidenceTitle;
    @FXML
    TextArea textAreaEvidenceDescription;
    @FXML
    private HBox hboxLogOutLabel;
    private static final int MAX_TITLE_EVIDENCE_LENGTH = 30;
    private static final int MAX_DESCRIPTION_EVIDENCE_LENGTH = 100;
    private static final Logger logger = Logger.getLogger(ModifyEvidenceController.class);

    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        showEvidenceDetailsToModify();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    private String getAdvancementName() throws SQLException{
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        return evidenceDAO.getAdvancementNameByStudentID(SessionDetails.getInstance().getId(),
                evidenceDAO.getAdvancementIDByEvidenceID(TransferEvidence.getEvidenceId()));
    }
    @FXML
    private void modifyEvidence() {
        if (fieldsCorrect() && confirmedModify()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Evidence evidence = new Evidence();

            evidence.setEvidenceTitle(textFieldEvidenceTitle.getText());
            evidence.setEvidenceDescription(textAreaEvidenceDescription.getText());
            evidence.setEvidenceId(TransferEvidence.getEvidenceId());
            
            try {
                if (evidenceDAO.modifyEvidence(evidence) == 1){
                    DialogGenerator.getDialog(new AlertMessage("La evidencia ha sido actualizada con éxito.",
                            AlertStatus.SUCCESS));
                }
            } catch (SQLException modifyEvidenceException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No hay conexión a la base de datos, no se pudo modificar la evidencia.",
                        AlertStatus.ERROR));
                logger.error(modifyEvidenceException);
            }
        }
    }
    
    private void showEvidenceDetailsToModify() {
        try {
            Evidence evidenceDetails = getEvidenceToModify();
            labelAdvancementTitle.setText(getAdvancementName());
            textFieldEvidenceTitle.setText(evidenceDetails.getEvidenceTitle());
            textAreaEvidenceDescription.setText(evidenceDetails.getEvidenceDescription());
        } catch (SQLException evidenceDetailsException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo recuperar" +
                    " la información de la evidencia a modificar.", AlertStatus.ERROR));
            logger.error(evidenceDetailsException);
        }
    }
    
    private Evidence getEvidenceToModify() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        return evidenceDAO.getEvidenceByEvidenceID(TransferEvidence.getEvidenceId());
    }

    @FXML
    private void redirectToFiles() throws IOException {
        MainStage.changeView("evidencefiles-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    public boolean emptyFields() {
        return textFieldEvidenceTitle.getText().isBlank()
                || textAreaEvidenceDescription.getText().isBlank();
    }

    private boolean overSizeData() {
        return textFieldEvidenceTitle.getText().length() > MAX_TITLE_EVIDENCE_LENGTH
                || textAreaEvidenceDescription.getText().length() > MAX_DESCRIPTION_EVIDENCE_LENGTH;
    }

    private boolean fieldsCorrect() {
        boolean result = false;
        if (emptyFields()) {
            if (textFieldEvidenceTitle.getText().isBlank()){
                DialogGenerator.getDialog(new AlertMessage("Debe ingresar un titulo a la evidencia", AlertStatus.WARNING));
            } else if (textAreaEvidenceDescription.getText().isBlank()) {
                DialogGenerator.getDialog(new AlertMessage("Debe ingresar una descripción para la evidencia", AlertStatus.WARNING));
            }
        } else {
            if (overSizeData()) {
                if (textFieldEvidenceTitle.getText().length() > MAX_TITLE_EVIDENCE_LENGTH){
                    DialogGenerator.getDialog(new AlertMessage("El título de la evidencia excede el límite de caracteres: " + MAX_TITLE_EVIDENCE_LENGTH, AlertStatus.WARNING));
                } else if (textAreaEvidenceDescription.getText().length() > MAX_DESCRIPTION_EVIDENCE_LENGTH) {
                    DialogGenerator.getDialog(new AlertMessage("La descripción de la evidencia excede el límite de caracteres: " + MAX_DESCRIPTION_EVIDENCE_LENGTH, AlertStatus.WARNING));
                }
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean confirmedModify() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Estás seguro que deseas modificar la evidencia?");
        return response.get() == DialogGenerator.BUTTON_YES;
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
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
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
