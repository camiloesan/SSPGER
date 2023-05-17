package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.TransferStudent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgressReportController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelStudent;
    @FXML
    private Label labelDirectors;
    @FXML
    private Label labelReceptionWork;
    @FXML
    private Label labelDate;
    LocalDate actualDate = LocalDate.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        setNamesLabels();
        labelDate.setText(actualDate.format(dateFormat));
    }
    
    private void setNamesLabels() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        labelStudent.setText(TransferStudent.getStudentName());
        labelDirectors.setText(professorDAO.getProfessorsByProject(TransferProject.getProjectID()));
        labelReceptionWork.setText(TransferProject.getReceptionWorkName());
    }
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        try {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
