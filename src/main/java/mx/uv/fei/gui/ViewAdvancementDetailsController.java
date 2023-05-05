package mx.uv.fei.gui;

import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class ViewAdvancementDetailsController implements IProfessorNavigationBar{
    
    @Override
    public void redirectToAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",700, 500 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjectManagement() throws IOException {
        MainStage.changeView("timeline-view.fxml", 700, 500 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToEvidences() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
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
