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
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Optional;

public class ModifyEvidenceController implements IStudentNavigationBar {
    @FXML
    Label labelAdvancementTitle;
    @FXML
    Label labelFileTitle;
    @FXML
    TextField textFieldEvidenceTitle;
    @FXML
    TextArea textAreaEvidenceDescription;

    @FXML
    public void initialize() {
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
        if (fullFields() && confirmedModify()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Evidence evidence = new Evidence();

            evidence.setEvidenceTitle(textFieldEvidenceTitle.getText());
            evidence.setEvidenceDescription(textAreaEvidenceDescription.getText());
            evidence.setEvidenceId(TransferEvidence.getEvidenceId());

            try {
                evidenceDAO.modifyEvidence(evidence);
            } catch (SQLException modifyEvidenceException) {
                modifyEvidenceException.printStackTrace();
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
    private boolean fullFields() {
        boolean result;
        if (textFieldEvidenceTitle.getText().isBlank() || textAreaEvidenceDescription.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Rellena los campos correctamente", AlertStatus.WARNING));
            result = false;
        } else {
            result = true;
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

    private Path createPath(String projectID, String advancementName, String studentName) {
        File path = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectID+"/"
                +advancementName+"/"+studentName);
        if (path.exists() == false) {
            path.mkdirs();
        }
        return path.toPath();
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

    @Override
    public void actionLogOut() throws IOException {

    }
}
