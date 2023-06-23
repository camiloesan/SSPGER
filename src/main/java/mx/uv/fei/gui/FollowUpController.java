package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import org.apache.log4j.Logger;

public class FollowUpController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ListView<Student> listViewStudents;
    @FXML
    private HBox hboxLogOutLabel;
    private static final Logger logger = Logger.getLogger(FollowUpController.class);
    
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        try {
            fillStudentList();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo recuperar la información.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
        setStudentNames();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private int getTransferProjectID() {
        return TransferProject.getProjectID();
    }
    
    @FXML
    private void fillStudentList() throws SQLException {
        StudentDAO studentDAO = new StudentDAO();
        int projectID = getTransferProjectID();
        listViewStudents.getItems().clear();
        listViewStudents.getItems().addAll(studentDAO.getStudentsByProjectID(projectID));
    }
    
    private void setStudentNames() {
        listViewStudents.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Student item, boolean empty){
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getFullName());
                }
            }
        });
    }
    
    @FXML
    private void openProgressReport() throws IOException {
        if (listViewStudents.getSelectionModel().getSelectedItem() != null) {
            TransferStudent.setStudentID(listViewStudents.getSelectionModel().getSelectedItem().getStudentID());
            TransferStudent.setStudentName(listViewStudents.getSelectionModel().getSelectedItem().getFullName());
            MainStage.changeView("progressreport-view.fxml",1120,700);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un Alumno para generar un reporte", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (Objects.equals(SessionDetails.getInstance().getUserType(), LoginController.USER_REPRESENTATIVE)) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (Objects.equals(SessionDetails.getInstance().getUserType(), LoginController.USER_PROFESSOR)){
            MainStage.changeView(
                    "professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
