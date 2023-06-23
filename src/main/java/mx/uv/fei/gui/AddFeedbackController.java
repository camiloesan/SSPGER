package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    @FXML
    private HBox hboxLogOutLabel;
    private static final int MAX_TEXT_FEEDBACK_LENGTH = 850;
    private static final Logger logger = Logger.getLogger(AddFeedbackController.class);

    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        fillGradeCombo();
        showFeedbackInformation();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void showFeedbackInformation() {
        try {
            showFeedback();
            showEvidenceInformation();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo " +
                    "recuperar la información necesaria para la retroalimentación.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    public void showEvidenceInformation() throws SQLException{
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        Evidence evidenceDetails = getEvidenceInfo();
        labelProjectName.setText(evidenceDAO.getProjectNameByEvidenceID(TransferEvidence.getEvidenceId()));
        labelAdvancementName.setText("Avance: " + evidenceDetails.getAdvancementName());
        labelEvidenceName.setText("Evidencia: " + evidenceDetails.getEvidenceTitle());
        labelStudentName.setText("Estudiante: " + evidenceDetails.getStudentName());
    }
    
    public void fillGradeCombo() {
        comboBoxGrade.getItems().clear();
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

            feedback.setEvidenceID(TransferEvidence.getEvidenceId());
            feedback.setFeedbackText(textAreaFeedback.getText());

            try {
                if (feedbackDAO.addFeedback(feedback) == 1) {
                    evidenceDAO.updateEvidenceGradeCheckById(feedback.getEvidenceID(), comboBoxGrade.getValue());
                    showFeedback();
                }
            } catch (SQLException feedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No hay conexión a la base de datos, no se pudo registrar la retroalimentación",
                        AlertStatus.ERROR));
                logger.error(feedbackException);
            }
        }
    }

    @FXML
    private void deleteFeedback() {
        if (isConfirmedDeleteFeedback()) {
            EvidenceDAO evidenceDAO = new EvidenceDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();

            try {
                if (feedbackDAO.deleteFeedbackByID(feedbackDAO.getFeedbackIDByEvidenceID(
                        TransferEvidence.getEvidenceId(),
                        TransferEvidence.getStudentID())) == 1) {
                    evidenceDAO.updateEvidenceGradeUncheckById(TransferEvidence.getEvidenceId());
                    DialogGenerator.getDialog(new AlertMessage("Retroalimentación eliminada",
                            AlertStatus.SUCCESS));
                    showFeedback();
                }
            } catch (SQLException deleteFeedbackException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error al eliminar la retroalimentación", AlertStatus.ERROR));
                logger.error(deleteFeedbackException);
            }
        }
    }

    private void showFeedback() throws SQLException{
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        
        String studentID = evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId());
        
        if (getFeedbacks() != 0) {
            buttonDeleteFeedback.setVisible(true);
            buttonFeedback.setVisible(false);
            textAreaFeedback.setVisible(false);
            vBoxFeedbackText.setVisible(true);
            comboBoxGrade.setVisible(false);
            
            String textToFeedback;
            labelGrade.setText("Calificación: " + getEvidenceInfo().getEvidenceGrade());
            textToFeedback = feedbackDAO.getFeedbackTextByEvidenceID(TransferEvidence.getEvidenceId(), studentID);
            
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

    private int getFeedbacks() throws SQLException {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        return feedbackDAO.getFeedbacksByEvidenceID(TransferEvidence.getEvidenceId(), TransferEvidence.getStudentID());
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
                    "Debe ingresar la retroalimentación", AlertStatus.WARNING));
        } else {
            if (textAreaFeedback.getText().length() > MAX_TEXT_FEEDBACK_LENGTH) {
                DialogGenerator.getDialog(new AlertMessage(
                        "La retroalimentación excede el límite de caracteres: " + MAX_TEXT_FEEDBACK_LENGTH,
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


    private Evidence getEvidenceInfo() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        return evidenceDAO.getEvidenceInfoByID(TransferEvidence.getEvidenceId());
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
