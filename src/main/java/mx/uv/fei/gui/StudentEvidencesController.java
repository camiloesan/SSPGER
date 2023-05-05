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
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.Evidence;
import mx.uv.fei.logic.ProjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;

public class StudentEvidencesController implements IStudentNavigationBar {
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
    private TextField textFieldEvidenceID;
    @FXML
    private TextField textFieldEvidenceTitle;
    @FXML
    private TextField textFieldEvidenceDescription;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextField textFieldAdvancement;
    @FXML
    private TextField textFieldStudent;
    @FXML
    private TableView<Evidence> tableViewEvidence;
    @FXML
    private ListView<String> listViewEvidencesNamesToDelete;
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

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() {

    }

    @Override
    public void redirectToRequest() {

    }

    @FXML
    private void fillTableViewEvidence() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidence.getItems().addAll(evidenceDAO
                .getEvidenceListByStudent(LoginController.sessionDetails.getId()));
    }
    @FXML
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
    }

    @FXML
    public void deleteEvidence() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String evidenceName = listViewEvidencesNamesToDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea eliminar la evidencia " + evidenceName + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            try {
                evidenceDAO.deleteEvidenceByName(evidenceName);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    @FXML
    private void onActionAddFileButton() {
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
    private void onActionDeleteFileButton() {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("/home/danae/IdeaProjects/SSPGER/evidences");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Eliminar evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        evidenceFile.delete();
    }

    @FXML
    private void onActionAddEvidenceButton(){
        Evidence evidence = new Evidence();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Estás seguro que deseas añadir la evidencia?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            evidence.setEvidenceTitle(textFieldTitle.getText());
            evidence.setEvidenceDescription(textAreaDescription.getText());
            evidence.setAdvancementId(Integer.parseInt(textFieldAdvancement.getText()));
            evidence.setStudentId(textFieldStudent.getText());
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            try {
                evidenceDAO.addEvidence(evidence);
            } catch (SQLException exceptionAdd) {
                exceptionAdd.getErrorCode();
            }
        }
    }
    @FXML
    private void onActionButtonModify(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Estás seguro que deseas modificar la evidencia?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            int evidenceID = Integer.parseInt(textFieldEvidenceID.getText());
            String evidenceTitle = textFieldEvidenceTitle.getText();
            String evidenceDescription = textFieldEvidenceDescription.getText();
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            try {
                evidenceDAO.modifyEvidence(evidenceID, evidenceTitle, evidenceDescription);
            } catch (SQLException exceptionModify) {
                exceptionModify.getErrorCode();
            }
        }
    }

    @FXML
    private void updateListViewEvidencesToDelete() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        listViewEvidencesNamesToDelete.getItems().clear();
        for(Evidence evidenceObject : evidenceDAO.getListEvidenceName()) {
            listViewEvidencesNamesToDelete.getItems().add(evidenceObject.getEvidenceTitle());
        }
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
