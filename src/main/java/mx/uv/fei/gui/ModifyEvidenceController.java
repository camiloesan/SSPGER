package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.EvidenceDAO;
import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;

public class ModifyEvidenceController {
    @FXML
    private TextField textFieldEvidenceID;
    @FXML
    private TextField textFieldEvidenceTitle;
    @FXML
    private TextField textFieldEvidenceDescription;

    @FXML
    private void onActionButtonModify(){
        int evidenceID = Integer.parseInt(textFieldEvidenceID.getText());
        String evidenceTitle = textFieldEvidenceTitle.getText();
        String evidenceDescription = textFieldEvidenceDescription.getText();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        try{
            evidenceDAO.modifyEvidence(evidenceID,evidenceTitle,evidenceDescription);
        } catch(SQLException exceptionModify){
            exceptionModify.getErrorCode();
        }
    }
}
