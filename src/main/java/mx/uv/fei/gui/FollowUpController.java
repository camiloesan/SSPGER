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
import mx.uv.fei.logic.Student;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.TransferStudent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class FollowUpController implements IProfessorNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ListView<Student> listViewStudents;
    @FXML
    private HBox hboxLogOutLabel;
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillStudentList();
        setStudentNames();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private int getTransferProjectID() {
        return TransferProject.getProjectID();
    }
    
    @FXML
    private void fillStudentList() throws SQLException {
        StudentDAO studentDAO = new StudentDAO();
        listViewStudents.getItems().clear();
        listViewStudents.getItems().addAll(studentDAO.getStudentsByProject(getTransferProjectID()));
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
        TransferStudent.setStudentName(listViewStudents.getSelectionModel().getSelectedItem().getFullName());
        MainStage.changeView("progressreport-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
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
