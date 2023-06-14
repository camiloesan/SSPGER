package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.FeedbackDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AddFeedbackController implements IProfessorNavigationBar {
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
    @FXML
    private VBox vBoxFeedbackText;
    @FXML
    private Button buttonDeleteFeedback;
    @FXML
    private Button buttonFeedback;
    @FXML
    private TextArea textAreaFeedback;
    @FXML
    private ComboBox<Integer> comboBoxGrade;
    private static final int MAX_TEXT_FEEDBACK_LENGTH = 850;
    private static final Logger logger = Logger.getLogger(AddFeedbackController.class);

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
        for (int i = 1; i <= 10; i++) {
            comboBoxGrade.getItems().add(i);
        }
    }

    @FXML
    private void registerFeedback() {

        if (isTextAreaCorrect() && isGradeSelect() && isConfirmedFeedback()) {
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            Feedback feedback = new Feedback();
            int result = 0;

            feedback.setEvidenceID(TransferEvidence.getEvidenceId());
            feedback.setFeedbackText(textAreaFeedback.getText());

            try {
                result = feedbackDAO.addFeedback(feedback);
                evidenceDAO.updateEvidenceGradeCheckById(feedback.getEvidenceID(), comboBoxGrade.getValue());
            } catch (SQLException feedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error al registrar la retroalimentación", AlertStatus.ERROR));
                logger.error(feedbackException);
            }

            if (result == 1) {
                DialogGenerator.getDialog(new AlertMessage("Retroalimentación guardada", AlertStatus.SUCCESS));
                showFeedback();
            }
        }
    }

    @FXML
    private void deleteFeedback() {
        if (isConfirmedDeleteFeedback()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            int result = 0;

            try {
                result = feedbackDAO.deleteFeedbackByID(
                        feedbackDAO.getFeedbackIDByEvidenceID(TransferEvidence.getEvidenceId(),
                        TransferEvidence.getStudentID()));
                evidenceDAO.updateEvidenceGradeUncheckById(TransferEvidence.getEvidenceId());
            } catch (SQLException deleteFeedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error al eliminar la retroalimentación", AlertStatus.ERROR));
                logger.error(deleteFeedbackException);
            }

            if (result == 1) {
                DialogGenerator.getDialog(new AlertMessage("Retroalimentación eliminada", AlertStatus.SUCCESS));
                showFeedback();
            }

        }
    }

    private void showFeedback() {
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        if (getFeedbacks() != 0) {
            buttonDeleteFeedback.setVisible(true);
            buttonFeedback.setVisible(false);
            textAreaFeedback.setVisible(false);
            vBoxFeedbackText.setVisible(true);
            comboBoxGrade.setVisible(false);
            labelGrade.setText("Calificación: " + getEvidenceInfo().getEvidenceGrade());
            String textToFeedback = null;

            try {
                textToFeedback = feedbackDAO.getFeedbackTextByEvidenceID(TransferEvidence.getEvidenceId(),
                        TransferEvidence.getStudentID());
            } catch (SQLException textFeedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo recuperar la retroalimentación", AlertStatus.ERROR));
                logger.error(textFeedbackException);
            }
            textFeedback.setText("Retroalimentación: " + textToFeedback);
            textFeedback.setVisible(true);
        } else {
            buttonDeleteFeedback.setVisible(false);
            buttonFeedback.setVisible(true);
            textAreaFeedback.setVisible(true);
            vBoxFeedbackText.setVisible(false);
            textFeedback.setVisible(false);
            comboBoxGrade.setVisible(true);
            labelGrade.setText("Calificación: ");
        }

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

    private boolean isGradeSelect() {
        boolean result = true;

        if (comboBoxGrade.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Selecciona una calificación", AlertStatus.WARNING));
            result = false;
        }

        return result;
    }

    private boolean isTextAreaCorrect() {
        boolean result = false;
        if (textAreaFeedback.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Rellena la retroalimentación", AlertStatus.WARNING));
        } else {
            if (textAreaFeedback.getText().length() > MAX_TEXT_FEEDBACK_LENGTH) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Excediste el máximo de carateres (850 para la retroalimentación)",
                        AlertStatus.WARNING));
            } else {
                result = true;
            }
        }
        return result;
    }

    private boolean isConfirmedFeedback() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que deseas retroalimentar esta evidencia?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    private boolean isConfirmedDeleteFeedback() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que deseas eliminar la retroalimentación?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
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
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }

}
