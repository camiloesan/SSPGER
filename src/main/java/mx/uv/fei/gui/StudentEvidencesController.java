package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

        TableColumn<Advancement, String> nameAdvancement = new TableColumn<>("Nombre Evidencia");
        nameAdvancement.setCellValueFactory(new PropertyValueFactory<>("advancementName"));
        TableColumn<Advancement, String> starAdvancement = new TableColumn<>("Fecha de inicio");
        starAdvancement.setCellValueFactory(new PropertyValueFactory<>("advancementStartDate"));
        TableColumn<Advancement, String> finishAdvancement = new TableColumn<>("Fecha de inicio");
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
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("evidences.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() {

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
            MainStage.changeView("addevidence-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @FXML
    public void redirectToModifyEvidence() throws IOException {
        MainStage.changeView("modifyevidence-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @FXML
    public void redirectToViewEvidenceDetails() throws IOException {
        MainStage.changeView("viewevidencedetails-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @FXML
    private void fillTableViewEvidence() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidence.getItems().addAll(evidenceDAO
                .getEvidenceListByStudent(LoginController.sessionDetails.getId()));
    }

    @FXML
    private void fillTableViewAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
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

    /*@FXML
    public void fillTitleStatusGradeDescriptionEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try {
            Evidence evidence = evidenceDAO.getEvidenceByEvidenceTitle(tableViewEvidence
                    .getSelectionModel()
                    .getSelectedItem().getEvidenceTitle());
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
    }*/

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
    @FXML
    private void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        File file = new File("/home/danae/IdeaProjects/SSPGER/evidences/"+evidenceFile.getName());
        try {
            Files.copy(evidenceFile.toPath(), file.toPath());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    @FXML
    private void deleteFile() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("/home/danae/IdeaProjects/SSPGER/evidences");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Eliminar evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        evidenceFile.delete();
    }




    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }


}
