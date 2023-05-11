package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.Evidence;
import mx.uv.fei.logic.TransferEvidence;

import java.io.IOException;
import java.sql.SQLException;

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
    private void initialize() {
        fillEvidence();
    }

    @FXML
    public void fillTitleStatusGradeDescriptionEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try {
            Evidence evidence = evidenceDAO.getEvidenceByEvidenceTitle(TransferEvidence.getEvidenceName());
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
            int advancementID = evidenceDAO.getAdvancementIDByEvidenceTitle(TransferEvidence.getEvidenceName());
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
            String studentID = evidenceDAO.getStudentIDByEvidenceTitle(TransferEvidence.getEvidenceName());
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
        fillAdvancementEvidence();
        fillStudentEvidence();
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

    @Override
    public void actionLogOut() throws IOException {

    }
}
