package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.EvidenceDAO;
import mx.uv.fei.logic.Evidence;

import java.io.File;
import java.sql.SQLException;

public class AddEvidenceController {
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
    private void onActionAddFileButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        fileChooser.setInitialDirectory(new File("/home"));
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
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
}
