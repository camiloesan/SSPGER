package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.AdvancementDAO;
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
    private ListView<Advancement> listViewAdvancementsNames;
    @FXML
    private HBox hboxLogOutLabel;
    
    private static final Logger logger = Logger.getLogger(StudentAdvancementsController.class);
    
    @FXML
    private void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        fillListViewAdvancements();
        setAdvancementNames();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    @FXML
    private void fillListViewAdvancements() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            String studentId = LoginController.sessionDetails.getId();
            listViewAdvancementsNames.getItems().clear();
            
            List<Advancement> advancementList = new ArrayList<>(advancementDAO.getListAdvancementNamesByStudentId(studentId));
            listViewAdvancementsNames.getItems().addAll(advancementList);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar los avances programados.",AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    private void setAdvancementNames() {
        listViewAdvancementsNames.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Advancement item, boolean empty){
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getAdvancementName());
                }
            }
        });
    }
    
    @FXML
    private void viewAdvancementDetails() throws IOException {
        if (listViewAdvancementsNames.getSelectionModel().getSelectedItem() != null) {
            int advancementID = listViewAdvancementsNames.getSelectionModel().getSelectedItem().getAdvancementID();
            TransferAdvancement.setAdvancementID(advancementID);
            MainStage.changeView("studentviewadvancementdetails-view.fxml",1000,600);
        } else {
            DialogGenerator.getDialog(new AlertMessage(
                    "Seleccione un avance para ver los detalles.", AlertStatus.WARNING));
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
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
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
