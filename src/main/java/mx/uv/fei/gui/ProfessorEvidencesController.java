package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.Evidence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProfessorEvidencesController implements IProfessorNavigationBar {
    @FXML
    private Label labelTitleEvidence;
    @FXML
    private Label labelStatusEvidence;
    @FXML
    private Label labelGradeEvidence;
    @FXML
    private Label labelDescriptionEvidence;
    @FXML
    private Label labelAdvancementEvidence;
    @FXML
    private Label labelStudentEvidence;
    @FXML
    private ListView<String> listViewEvidencesName;

    @FXML
    public void fillTitleStatusGradeDescriptionEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try {
            Evidence evidence = evidenceDAO.getEvidenceByEvidenceTitle(listViewEvidencesName
                    .getSelectionModel()
                    .getSelectedItem());
            labelTitleEvidence.setText(evidence.getEvidenceTitle());
            labelStatusEvidence.setText(evidence.getEvidenceStatus());
            labelGradeEvidence.setText(String.valueOf(evidence.getEvidenceGrade()));
            labelDescriptionEvidence.setText(evidence.getEvidenceDescription());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
    @FXML
    public void fillAdvancementEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            int advancementID = evidenceDAO.getAdvancementIDByEvidenceTitle(listViewEvidencesName
                    .getSelectionModel()
                    .getSelectedItem());
            labelAdvancementEvidence.setText("");
            try {
                String advancementName = advancementDAO.getAdvancementNameByID(advancementID);
                labelAdvancementEvidence.setText(advancementName);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    @FXML
    public void fillStudentEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        StudentDAO studentDAO = new StudentDAO();
        try {
            String studentID = evidenceDAO.getStudentIDByEvidenceTitle(listViewEvidencesName
                    .getSelectionModel()
                    .getSelectedItem());
            try {
                String nameStudent = studentDAO.getNamebyStudentID(studentID);
                labelStudentEvidence.setText(nameStudent);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    @FXML
    public void fillEvidence() {
        fillTitleStatusGradeDescriptionEvidence();
        labelTitleEvidence.setOpacity(1);
        labelStatusEvidence.setOpacity(1);
        labelGradeEvidence.setOpacity(1);
        labelDescriptionEvidence.setOpacity(1);
        fillAdvancementEvidence();
        labelAdvancementEvidence.setOpacity(1);
        fillStudentEvidence();
        labelStudentEvidence.setOpacity(1);
    }

    @FXML
    private void updateListViewEvidences() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        listViewEvidencesName.getItems().clear();
        for(Evidence evidenceObject : evidenceDAO.getListEvidenceName()) {
            listViewEvidencesName.getItems().add(evidenceObject.getEvidenceTitle());
        }
    }

    @Override
    public void redirectToAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
