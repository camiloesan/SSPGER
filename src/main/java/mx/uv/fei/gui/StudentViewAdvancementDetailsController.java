package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.SessionDetails;
import mx.uv.fei.logic.TransferAdvancement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentViewAdvancementDetailsController implements IStudentNavigationBar{
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelAdvancementName;
    @FXML
    private TextFlow textAdvancementDescription;
    @FXML
    private Label labelStartDate;
    @FXML
    private Label labelDeadline;
    @FXML
    private HBox hboxLogOutLabel;
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        getDetailedAdvancement();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public String getAdvancementName() {
        return TransferAdvancement.getAdvancementName();
    }
    
    public void getDetailedAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement detaildedAdvancement = (advancementDAO.getAdvancementDetailById(TransferAdvancement.getAdvancementID()));
        
        labelAdvancementName.setText(detaildedAdvancement.getAdvancementName());
        
        Text advancementDescription = new Text(detaildedAdvancement.getAdvancementDescription());
        textAdvancementDescription.getChildren().add(advancementDescription);
        
        labelStartDate.setText(detaildedAdvancement.getAdvancementStartDate());
        
        labelDeadline.setText(detaildedAdvancement.getAdvancementDeadline());
    }
    
    public void returnToAdvancementList() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
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
