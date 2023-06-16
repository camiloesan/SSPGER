package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.FeedbackDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ViewFeedbackController implements IStudentNavigationBar {
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelProjectName;
    @FXML
    private Label labelAdvancementName;
    @FXML
    private Label labelEvidenceName;
    @FXML
    private Label labelStudentName;
    @FXML
    private Label labelGrade;
    @FXML
    private Text textFeedback;
    private static final Logger logger = Logger.getLogger(ViewFeedbackController.class);

    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        EvidenceDAO evidenceDAO = new EvidenceDAO();

        try {
            labelProjectName.setText(evidenceDAO.getProjectNameByEvidenceID(TransferEvidence.getEvidenceId()));
        } catch (SQLException projectNameException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo recuperar el nombre del proyecto", AlertStatus.ERROR));
            logger.error(projectNameException);
        }

        labelAdvancementName.setText("Avance: " + getEvidenceInfo().getAdvancementName());
        labelEvidenceName.setText("Evidencia: " + getEvidenceInfo().getEvidenceTitle());
        labelStudentName.setText("Estudiante: " + getEvidenceInfo().getStudentName());
        showFeedback();
    }


    private int getFeedbacks() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        int numberOfFeedbacks = 0;

        try {
            numberOfFeedbacks = feedbackDAO.getFeedbacksByEvidenceID(TransferEvidence.getEvidenceId(),
                    TransferEvidence.getStudentID());
        } catch (SQLException numberOfFeedbacksException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar el número de retroalimentaciones", AlertStatus.ERROR));
            logger.error(numberOfFeedbacksException);
        }

        return numberOfFeedbacks;
    }

    private void showFeedback() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        if (getFeedbacks() != 0) {
            labelGrade.setText("Calificación: " + getEvidenceInfo().getEvidenceGrade());
            try {
                textFeedback.setText("Retroalimentación: " +
                        feedbackDAO.getFeedbackTextByEvidenceID(TransferEvidence.getEvidenceId(),
                                TransferEvidence.getStudentID()));
            } catch (SQLException textFeedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo recuperar la retroalimentación", AlertStatus.ERROR));
                logger.error(textFeedbackException);
            }
        } else {
            labelGrade.setText("Sin calificación");
            textFeedback.setText("Sin retroalimentación");
        }

    }

    private Evidence getEvidenceInfo() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Evidence evidence = new Evidence();

        try {
            evidence = evidenceDAO.getEvidenceInfoByID(TransferEvidence.getEvidenceId());
        } catch (SQLException evidenceInfoException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo recuperar la información de la evidencia", AlertStatus.ERROR));
            logger.error(evidenceInfoException);
        }

        return evidence;
    }

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() throws IOException {
        MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }

}
