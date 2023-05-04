package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class StudentViewProjectsController implements IStudentNavigationBar{
    @FXML
    private ListView<String> listViewVerifiedProjects;
    @FXML
    private Button buttonActualizar;
    @FXML
    private Button buttonVerDetalles;
    @FXML
    private HBox hboxLogOutLabel;
    
    private static final String VERIFIED_PROJECT_STATUS = "Verificado";
    
    public void initialize() throws SQLException {
        fillListViewProjects();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void fillListViewProjects() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewVerifiedProjects.getItems().clear();
        
        ArrayList<DetailedProject> proposedProjects = new ArrayList<>(projectDAO.getProjectsByState(VERIFIED_PROJECT_STATUS));
        proposedProjects.forEach(element -> listViewVerifiedProjects.getItems().add(element.getProjectTitle()));
    }
    
    public void openProjectDetails() throws IOException {
        if (listViewVerifiedProjects.getSelectionModel().getSelectedItem() != null) {
            String receptionWorkName = listViewVerifiedProjects.getSelectionModel().getSelectedItem();
            TransferProject.setReceptionWorkName(receptionWorkName);
            MainStage.changeView("studentviewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToEvidences() throws  IOException {
        MainStage.changeView("studentevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjects() {
    
    }
    
    @Override
    public void redirectToRequest() {
    
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
