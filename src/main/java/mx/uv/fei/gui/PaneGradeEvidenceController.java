package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.TransferEvidence;

import java.io.IOException;
import java.sql.SQLException;

public class PaneGradeEvidenceController {
    @FXML
    private Label labelEvidenceToGrade;
    @FXML
    private TextField textFieldGrade;

    @FXML
    private void initialize() {
        labelEvidenceToGrade.setText(TransferEvidence.getEvidenceName());
    }

    private boolean isGradeValid() {
        if (textFieldGrade.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Debes escribir la calificación", AlertStatus.WARNING));
            return false;
        } else {
            int grade;
            try {
                grade = Integer.parseInt(textFieldGrade.getText());
            } catch (Exception e) {
                DialogGenerator.getDialog(new AlertMessage("El formato no es válido, inténtalo de nuevo", AlertStatus.WARNING));
                return false;
            }
            if (grade >= 0 && grade <= 10) {
                return true;
            } else {
                DialogGenerator.getDialog(new AlertMessage("El rango permitido es del 0 al 10", AlertStatus.WARNING));
                return false;
            }
        }
    }

    @FXML
    private void updateGrade() {
        if (isGradeValid()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            int grade = Integer.parseInt(textFieldGrade.getText());
            try {
                evidenceDAO.updateEvidenceGradeById(TransferEvidence.getEvidenceId(), grade);
                DialogGenerator.getDialog(new AlertMessage("Se actualizó la calificación satisfactoriamente", AlertStatus.SUCCESS));
            } catch (SQLException sqlException) {
                DialogGenerator.getDialog(new AlertMessage("No se pudo actualizar la calificación, inténtelo más tarde", AlertStatus.ERROR));
            }
        }
    }

    @FXML
    void returnToAdvancementList() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
}
