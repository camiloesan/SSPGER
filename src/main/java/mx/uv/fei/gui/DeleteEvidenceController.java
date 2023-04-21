package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.EvidenceDAO;
import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;

public class DeleteEvidenceController {
    @FXML
    private TextField textFieldTitle;
    @FXML
    private Button buttonDeleteEvidence;
    @FXML
    private void onActionDeleteEvidenceButton(){
        Evidence evidence = new Evidence();
        evidence.setEvidenceTitle(textFieldTitle.getText());
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try{
            evidenceDAO.deleteEvidenceByName(evidence.getEvidenceTitle());
        } catch(SQLException exceptionDelete){
            exceptionDelete.getErrorCode();
        }
    }
}
