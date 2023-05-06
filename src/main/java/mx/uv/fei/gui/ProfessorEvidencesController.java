package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.Evidence;
import mx.uv.fei.logic.TransferEvidence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
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
    private TableView<Evidence> tableViewEvidence;
    @FXML
    Tab tabEvidenceList;

    @FXML
    private void initialize() {
        TableColumn<Evidence, String> titleEvidence = new TableColumn<>("Título");
        titleEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        TableColumn<Evidence, String> statusEvidence = new TableColumn<>("Estado");
        statusEvidence.setCellValueFactory(new PropertyValueFactory<>("evidenceStatus"));
        tableViewEvidence.getColumns().addAll(titleEvidence, statusEvidence);
        try {
            fillTableViewEvidence();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo conectar con la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
            sqlException.printStackTrace();
        }
    }

    @FXML
    private void gradeEvidenceButtonAction() throws IOException {
        if (tableViewEvidence.getSelectionModel().getSelectedItem() != null) {
            int evidenceId = tableViewEvidence.getSelectionModel().getSelectedItem().getEvidenceId(); //?????????????????????????
            String evidenceName = tableViewEvidence.getSelectionModel().getSelectedItem().getEvidenceTitle();

            TransferEvidence.setEvidenceId(evidenceId);
            TransferEvidence.setEvidenceName(evidenceName);

            Parent gradeEvidenceVbox = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("panegradeevidence-view.fxml")));
            tabEvidenceList.setContent(gradeEvidenceVbox);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar una evidencia", AlertStatus.WARNING));
        }
    }

    @FXML
    private void fillTableViewEvidence() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        StudentDAO studentDAO = new StudentDAO();
        tableViewEvidence.getItems().addAll(evidenceDAO.getEvidenceListByStudent(studentDAO.getStudentIDByProfessorID(Integer.parseInt(LoginController.sessionDetails.getId()))));
    }
    @FXML
    public void fillTitleStatusGradeDescriptionEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try {
            Evidence evidence = evidenceDAO.getEvidenceByEvidenceTitle(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceTitle());
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
            int advancementID = evidenceDAO.getAdvancementIDByEvidenceTitle(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceTitle());
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
            String studentID = evidenceDAO.getStudentIDByEvidenceTitle(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem()
                    .getEvidenceTitle());
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

    @Override
    public void redirectToAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
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
