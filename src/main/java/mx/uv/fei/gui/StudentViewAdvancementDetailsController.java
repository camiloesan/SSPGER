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
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import org.apache.log4j.Logger;

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
    
    private static final Logger logger = Logger.getLogger(StudentViewAdvancementDetailsController.class);
    
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        showAdvancementDetails();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public String getAdvancementName() {
        return TransferAdvancement.getAdvancementName();
    }
    
    private void showAdvancementDetails() {
        try {
            getDetailedAdvancement();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar la información del avance.",
                    AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }
    
    public void getDetailedAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        int advancementID = TransferAdvancement.getAdvancementID();
        Advancement detailedAdvancement = (advancementDAO.getAdvancementDetailById(advancementID));
        
        labelAdvancementName.setText(detailedAdvancement.getAdvancementName());
        
        Text advancementDescription = new Text(detailedAdvancement.getAdvancementDescription());
        textAdvancementDescription.getChildren().add(advancementDescription);
        
        labelStartDate.setText(detailedAdvancement.getAdvancementStartDate());
        
        labelDeadline.setText(detailedAdvancement.getAdvancementDeadline());
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
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 +
                MainStage.HEIGHT_OFFSET);
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
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
