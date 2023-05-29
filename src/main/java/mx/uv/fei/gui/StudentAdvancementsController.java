package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

public class StudentAdvancementsController implements IStudentNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private ListView<String> listViewAdvancementsNames;
    @FXML
    private HBox hboxLogOutLabel;
    private static final Logger logger = Logger.getLogger(ProjectRequestsController.class);
    
    @FXML
    private void initialize()  {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        try {
            fillListViewAdvancements();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información.",AlertStatus.ERROR));
            logger.error(sqlException);
        }
        
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void fillListViewAdvancements() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        StudentDAO studentDAO = new StudentDAO();
        String studentId = studentDAO.getStudentIdByUsername(LoginController.sessionDetails.getUsername());
        
        listViewAdvancementsNames.getItems().clear();
        List<Advancement> advancementList = new ArrayList<>(advancementDAO.getListAdvancementNamesByStudentId(studentId));
        advancementList.forEach(element -> listViewAdvancementsNames.getItems().add(element.getAdvancementName()));
    }
    
    public void viewAdvancementDetails() throws IOException {
        if (listViewAdvancementsNames.getSelectionModel().getSelectedItem() != null) {
            String advancementName = listViewAdvancementsNames.getSelectionModel().getSelectedItem();
            TransferAdvancement.setAdvancementName(advancementName);
            MainStage.changeView("studentviewadvancementdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Seleccione un avance para ver los detalles.", AlertStatus.WARNING));
        }
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
    public void redirectToProjects() throws IOException{
        MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToRequest() {
    
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }
    
    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
