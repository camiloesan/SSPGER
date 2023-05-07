package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.TransferAdvancement;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class StudentAdvancementsController implements IStudentNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ListView<String> listViewAdvancementsNames;
    @FXML
    private HBox hboxLogOutLabel;
    
    @FXML
    private void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        //fillListViewAdvancements();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void fillListViewAdvancements() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        StudentDAO studentDAO = new StudentDAO();
        String studentId = studentDAO.getStudentIdByUsername(LoginController.sessionDetails.getUsername());
        
        listViewAdvancementsNames.getItems().clear();
        List<Advancement> advancementList = new ArrayList<>(advancementDAO.getListAdvancementNameStudent(studentId));
        advancementList.forEach(element -> listViewAdvancementsNames.getItems().add(element.getAdvancementName()));
    }
    
    public void viewAdvancementDetails() throws IOException {
        if (listViewAdvancementsNames.getSelectionModel().getSelectedItem() != null) {
            String advancementName = listViewAdvancementsNames.getSelectionModel().getSelectedItem();
            TransferAdvancement.setAdvancementName(advancementName);
            MainStage.changeView("studentviewadvancementdetails-view.fxml",900,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un avance para ver los detalles.", AlertStatus.WARNING));
        }
    }
    
    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 900, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjects() throws IOException{
        MainStage.changeView("studentviewprojects-view.fxml",900, 600 + MainStage.HEIGHT_OFFSET);
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
