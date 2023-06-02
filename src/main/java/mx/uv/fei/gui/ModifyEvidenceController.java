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
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        labelAdvancementTitle.setText(getAdvancementName());
    }

    private String getAdvancementName() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String nameAdvancement = null;
        try {
            nameAdvancement = evidenceDAO
                    .getAdvancementNameByStudentID(LoginController.sessionDetails.getId(),
                            evidenceDAO.getAdvancementIDByEvidenceID(TransferEvidence.getEvidenceId()));
        } catch (SQLException nameAdavancementException) {
            nameAdavancementException.printStackTrace();
        }
        return nameAdvancement;
    }
    @FXML
    private void modifyEvidence() {
        if (fieldsCorrect() && confirmedModify()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Evidence evidence = new Evidence();

            evidence.setEvidenceTitle(textFieldEvidenceTitle.getText());
            evidence.setEvidenceDescription(textAreaEvidenceDescription.getText());
            evidence.setEvidenceId(TransferEvidence.getEvidenceId());

            int resultDAO = 0;
            try {
                resultDAO = evidenceDAO.modifyEvidence(evidence);
            } catch (SQLException modifyEvidenceException) {
                modifyEvidenceException.printStackTrace();
            }
            if(resultDAO == 1) {
                DialogGenerator.getDialog(new AlertMessage
                        ("La evidencia ha sido actualizada con exito", AlertStatus.SUCCESS));
            } else {
                DialogGenerator.getDialog(new AlertMessage
                        ("Algo salió mal, su evidencia no fue guardad", AlertStatus.ERROR));
            }
        }
    }

    @FXML
    private void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        if (evidenceFile != null) {
            labelFileTitle.setText(evidenceFile.getName());
            labelFileTitle.setVisible(true);
            createPath(getProjectName(), getAdvancementName(), getStudentID());
            copyFile(evidenceFile);
        }
    }

    @FXML
    private void deleteFile() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                + getProjectName()+ "/"
                +getAdvancementName() +"/"
                +getStudentID());
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Eliminar evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        if (evidenceFile != null) {
            evidenceFile.delete();
        }
    }
    private boolean fieldsCorrect() {
        boolean result = false;
        if (textFieldEvidenceTitle.getText().isBlank() || textAreaEvidenceDescription.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Rellena los campos correctamente", AlertStatus.WARNING));
        } else {
            final int MAX_TITLE_EVIDENCE_LENGTH = 30;
            final int MAX_DESCRIPTION_EVIDENCE_LENGTH = 100;
            if (textFieldEvidenceTitle.getText().length() > MAX_TITLE_EVIDENCE_LENGTH
                    || textAreaEvidenceDescription.getText().length() > MAX_DESCRIPTION_EVIDENCE_LENGTH) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Exediste el máximo de carateres (30 para el título, 100 para los motivos)",
                        AlertStatus.WARNING));
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean confirmedModify() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Estás seguro que deseas modificar la evidencia?");
        return response.get() == DialogGenerator.BUTTON_YES;
    }

    private void copyFile(File file) {
        File fileToSave = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                + getProjectName()+ "/"
                +getAdvancementName() +"/"
                +getStudentID()+"/"
                +file.getName());
        try {
            Files.copy(file.toPath(), fileToSave.toPath());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getProjectName() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        String projectID = null;
        try {
            projectID = advancementDAO
                    .getProjectNameByStudentID(LoginController.sessionDetails.getId());
        } catch (SQLException getProjectIDException) {
            getProjectIDException.printStackTrace();
        }
        return projectID;
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

    @Override
    public void actionLogOut() throws IOException {

    }
}
