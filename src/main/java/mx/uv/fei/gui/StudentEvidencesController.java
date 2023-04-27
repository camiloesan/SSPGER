package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.EvidenceDAO;
import mx.uv.fei.logic.Evidence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class StudentEvidencesController implements IStudentNavigationBar{
    @FXML
    private TextField textFieldTitle;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private TextField textFieldProfessor;
    @FXML
    private TextField textFieldAdvancement;
    @FXML
    private TextField textFieldProject;
    @FXML
    private TextField textFieldStudent;
    @FXML
    private Button addEvidenceButton;
    @FXML
    private Button addFileButton;
    @FXML
    private ListView<String> listViewEvidencesName;
    @FXML
    private void initialize() throws SQLException {
        updateListView();
    }
    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 800, 500);
    }

    @Override
    public void redirectToEvidences() throws IOException {

    }

    @Override
    public void redirectToProjects() {

    }

    @Override
    public void redirectToRequest() {

    }

    @Override
    public void actionLogOut() throws IOException {

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
    private void onActionAddEvidenceButton(){
        Evidence evidence = new Evidence();
        evidence.setEvidenceTitle(textFieldTitle.getText());
        evidence.setEvidenceDescription(textAreaDescription.getText());
        evidence.setProfessorId(Integer.parseInt(textFieldProfessor.getText()));
        evidence.setAdvancementId(Integer.parseInt(textFieldAdvancement.getText()));
        evidence.setProjectId(Integer.parseInt(textFieldProject.getText()));
        evidence.setStudentId(textFieldStudent.getText());
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try{
            evidenceDAO.addEvidence(evidence);
        } catch(SQLException exceptionAdd){
            exceptionAdd.getErrorCode();
        }
    }
    @FXML
    private void updateListView() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        listViewEvidencesName.getItems().clear();
        for(Evidence evidenceObject : evidenceDAO.getListEvidenceName()) {
            listViewEvidencesName.getItems().add(evidenceObject.getEvidenceTitle());
        }
    }
}
