package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class AddEvidenceController implements IStudentNavigationBar {
    @FXML
    Label labelAdvancementName;
    @FXML
    Label labelNameFile;
    @FXML
    Label labelUsername;
    @FXML
    TextField textFieldEvidenceTitle;
    @FXML
    TextArea textAreaEvidenceDescription;
    private static final int MAX_TITLE_EVIDENCE_LENGTH = 30;
    private static final int MAX_DESCRIPTION_EVIDENCE_LENGTH = 100;
    private static final Logger logger = Logger.getLogger(AddEvidenceController.class);

    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        labelAdvancementName.setText(TransferAdvancement.getAdvancementName());
    }

    @FXML
    private void sendEvidence() {
        if (fieldsCorrect() && confirmedEvidence()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Evidence evidence = new Evidence();

            evidence.setEvidenceTitle(textFieldEvidenceTitle.getText());
            evidence.setEvidenceDescription(textAreaEvidenceDescription.getText());
            evidence.setAdvancementId(TransferAdvancement.getAdvancementID());
            evidence.setStudentId(LoginController.sessionDetails.getId());

            int resultDAO= 0;
            try {
                resultDAO = evidenceDAO.addEvidence(evidence);
            } catch (SQLException addEvidenceException) {
                DialogGenerator.getDialog(new AlertMessage
                        ("Algo salió mal, vuelva a intentarlo más tarde", AlertStatus.ERROR));
                logger.error(addEvidenceException);
            }
            if(resultDAO == 1) {
                DialogGenerator.getDialog(new AlertMessage
                        ("La evidencia ha sido guardado con exito", AlertStatus.SUCCESS));
            } else {
                DialogGenerator.getDialog(new AlertMessage
                        ("Algo salió mal, su evidencia no fue guardad", AlertStatus.ERROR));
            }
        }
    }

    @FXML
    private void addFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        if (evidenceFile != null) {
            labelNameFile.setText(evidenceFile.getName());
            labelNameFile.setVisible(true);
            createPath(getProjectID(), getAdvancementName(), getStudentID());
            copyFile(evidenceFile);
        }
    }

    private void copyFile(File file) throws IOException {
        File fileToSave = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +getProjectID()+ "/"
                +getAdvancementName() +"/"
                +getStudentID()+"/"
                +file.getName());
        Files.copy(file.toPath(), fileToSave.toPath());
    }

    private String getProjectID() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        String projectID = null;
        try {
            projectID = advancementDAO
                    .getProjectNameByStudentID(LoginController.sessionDetails.getId());
        } catch (SQLException getProjectIDException) {
            DialogGenerator.getDialog(new AlertMessage
                    ("Algo salió mal, vuelva a intentarlo más tarde", AlertStatus.ERROR));
            logger.error(getProjectIDException);
        }
        return projectID;
    }

    private String getAdvancementName() {
        return TransferAdvancement.getAdvancementName();
    }

    private String getStudentID() {
        return LoginController.sessionDetails.getId();
    }

    private void createPath(String projectID, String advancementName, String studentName) {
        File path = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectID+"/"
                +advancementName+"/"+studentName);
        if (!path.exists()) {
            path.mkdirs();
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
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequest-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    private boolean fieldsCorrect() {
        boolean result = false;
        if (textFieldEvidenceTitle.getText().isBlank() || textAreaEvidenceDescription.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Rellena los campos correctamente", AlertStatus.WARNING));
        } else {
            if (textFieldEvidenceTitle.getText().length() > MAX_TITLE_EVIDENCE_LENGTH
                    || textAreaEvidenceDescription.getText().length() > MAX_DESCRIPTION_EVIDENCE_LENGTH) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Excediste el máximo de carateres (30 para el título, 100 para los motivos)",
                        AlertStatus.WARNING));
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean confirmedEvidence() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Estás seguro que deseas enviar la evidencia?");
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
