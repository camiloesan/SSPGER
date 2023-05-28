package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ViewEvidenceDetailsController implements IStudentNavigationBar {
    @FXML
    Label labelTitleEvidence;
    @FXML
    Label labelStatusEvidence;
    @FXML
    Label labelGradeEvidence;
    @FXML
    Label labelDescriptionEvidence;
    @FXML
    Label labelAdvancementEvidence;
    @FXML
    Label labelStudentEvidence;
    @FXML
    Label labelUsername;

    @FXML
    private void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillEvidence();
    }

    @FXML
    private void openPaneGradeEvidence() throws IOException {
        TransferEvidence.setEvidenceId(TransferEvidence.getEvidenceId());
        TransferEvidence.setEvidenceName(labelTitleEvidence.getText());
        MainStage.changeView("panegradeevidence-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }


    @FXML
    public void fillEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Evidence evidenceInfo = null;
        try {
            evidenceInfo = evidenceDAO.getEvidenceInfoByID(TransferEvidence.getEvidenceId());
        } catch (SQLException evidenceInfoException) {
            evidenceInfoException.printStackTrace();
        }
        labelTitleEvidence.setText(evidenceInfo.getEvidenceTitle());
        labelStatusEvidence.setText(evidenceInfo.getEvidenceStatus());
        labelGradeEvidence.setText(String.valueOf(evidenceInfo.getEvidenceGrade()));
        labelDescriptionEvidence.setText(evidenceInfo.getEvidenceDescription());
        labelAdvancementEvidence.setText(evidenceInfo.getAdvancementName());
        labelStudentEvidence.setText(evidenceInfo.getStudentName());
    }

    @FXML
    private void visualizeFiles() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencias");
        fileChooser.setInitialDirectory(getInitialDirectory());
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        if (evidenceFile != null) {
            String operativeSystem = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();
            if (operativeSystem.contains("win")) {
                runtime.exec("cmd /c start \"\" \"" + evidenceFile.getAbsolutePath() + "\"" );
            } else if (operativeSystem.contains("mac")) {
                runtime.exec("open " + evidenceFile.getAbsolutePath());
            } else if (operativeSystem.contains("nix")
                    || operativeSystem.contains("nux")
                    || operativeSystem.contains("bsd")) {
                runtime.exec("xdg-open " + evidenceFile.getAbsolutePath());
            } else {
                System.out.println("No se puede determinar el sistema operativo.");
            }
        }
    }

    private File getInitialDirectory() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String projectName = null;
        String studentID = null;
        try {
            projectName = evidenceDAO.getProjectNameByEvidenceID(TransferEvidence.getEvidenceId());
            studentID = evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId());
        } catch(SQLException evidenceDAOException) {
            evidenceDAOException.printStackTrace();
        }
        return new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectName+ "/"
                +labelAdvancementEvidence.getText()+"/"
                +studentID);
    }


    @Override
    public void redirectToAdvancements() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToProjects() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToRequest() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
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
