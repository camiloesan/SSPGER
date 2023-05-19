package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListCell;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentViewProjectsController implements IStudentNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ListView<SimpleProject> listViewVerifiedProjects;
    @FXML
    private HBox hboxLogOutLabel;
    
    private static final String VERIFIED_PROJECT_STATUS = "Verificado";
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillListViewProjects();
        setProjectTitles();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    @FXML
    private void fillListViewProjects() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        listViewVerifiedProjects.getItems().clear();
        listViewVerifiedProjects.getItems().addAll(projectDAO.getProjectsByState(VERIFIED_PROJECT_STATUS));
    }

    private void setProjectTitles() {
        listViewVerifiedProjects.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(SimpleProject item, boolean empty){
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getProjectTitle());
                }
            }
        });
    }
    
    @FXML
    private void openProjectDetails() throws IOException {
        if (listViewVerifiedProjects.getSelectionModel().getSelectedItem() != null) {
            int projectID = listViewVerifiedProjects.getSelectionModel().getSelectedItem().getProjectID();
            TransferProject.setProjectID(projectID);
            MainStage.changeView("studentviewprojectdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver los detalles.", AlertStatus.WARNING));
        }
    }

    @FXML
    private void openProjectTimeline() throws IOException {
        if (listViewVerifiedProjects.getSelectionModel().getSelectedItem() != null) {
            TransferProject.setProjectID(listViewVerifiedProjects.getSelectionModel().getSelectedItem().getProjectID());
            MainStage.changeView("timeline-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un proyecto para ver el cronograma", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToEvidences() throws  IOException {
        MainStage.changeView("studentevidences-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjects() {}
    
    @Override
    public void redirectToRequest() {}
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
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
