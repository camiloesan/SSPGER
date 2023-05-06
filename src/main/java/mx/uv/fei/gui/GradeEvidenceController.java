package mx.uv.fei.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.TransferEvidence;

import java.io.IOException;
import java.sql.SQLException;

public class GradeEvidenceController {
    @FXML
    private Button buttonReturn;

    @FXML
    private Label labelEvidenceToGrade;

    @FXML
    private TextField textFieldGrade;

    @FXML
    private VBox vboxAdvancementDetails;

    @FXML
    private void initialize() {
        labelEvidenceToGrade.setText(TransferEvidence.getEvidenceName());
        System.out.println(TransferEvidence.getEvidenceId());
    }

    private void validate() {
        if (textFieldGrade.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Debes escribir la calificación", AlertStatus.WARNING));
        } else {
            updateGrade();
        }
    }

    @FXML
    private void updateGrade() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        int grade = Integer.parseInt(textFieldGrade.getText());
        System.out.println(TransferEvidence.getEvidenceId());
        try {
            evidenceDAO.updateEvidenceGradeById(TransferEvidence.getEvidenceId(), grade);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo actualizar la calificación, inténtelo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    void returnToAdvancementList() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
}
