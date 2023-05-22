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
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Optional;

public class AddEvidenceController implements IStudentNavigationBar {
    @FXML
    Label labelAdvancementName;
    @FXML
    Label labelNameFile;
    @FXML
    TextField textFieldEvidenceTitle;
    @FXML
    TextArea textAreaEvidenceDescription;

    @FXML
    public void initialize() {
        labelAdvancementName.setText(TransferAdvancement.getAdvancementName());
    }

    @FXML
    private void sendEvidence() {
        if (fullFields() && confirmedEvidence()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Evidence evidence = new Evidence();

            evidence.setEvidenceTitle(textFieldEvidenceTitle.getText());
            evidence.setEvidenceDescription(textAreaEvidenceDescription.getText());
            evidence.setAdvancementId(TransferAdvancement.getAdvancementID());
            evidence.setStudentId(LoginController.sessionDetails.getId());

            try {
                evidenceDAO.addEvidence(evidence);
            } catch (SQLException addEvidenceException) {
                DialogGenerator.getDialog(new AlertMessage
                        ("Algo salió mal, vuelva a intentarlo más tarde", AlertStatus.ERROR));
                addEvidenceException.printStackTrace();
            }
            DialogGenerator.getDialog(new AlertMessage
                    ("La evidencia ha sido guardado con exito", AlertStatus.SUCCESS));
        }
    }

    @FXML
    private void addFile() {
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

    private void copyFile(File file) {
        File fileToSave = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +getProjectID()+ "/"
                +getAdvancementName() +"/"
                +getStudentID()+"/"
                +file.getName());
        try {
            Files.copy(file.toPath(), fileToSave.toPath());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getProjectID() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        int projectID = 0;
        try {
            projectID = advancementDAO
                    .getAdvancementIDByStudentID(LoginController.sessionDetails.getId());
        } catch (SQLException getProjectIDException) {
            getProjectIDException.printStackTrace();
        }
        return String.valueOf(projectID);
    }

    private String getAdvancementName() {
        return TransferAdvancement.getAdvancementName();
    }

    private String getStudentID() {
        return LoginController.sessionDetails.getId();
    }

    private Path createPath(String projectID, String advancementName, String studentName) {
        File path = new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectID+"/"
                +advancementName+"/"+studentName);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path.toPath();
    }

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() throws IOException {

    }

    @Override
    public void redirectToRequest() {

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

    private boolean confirmedEvidence() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Estás seguro que deseas enviar la evidencia?");
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
