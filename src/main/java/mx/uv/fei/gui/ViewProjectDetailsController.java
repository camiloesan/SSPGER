package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.TransferProject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ViewProjectDetailsController {
    @FXML
    private HBox hboxLogOutLabel;
    @FXML
    private Label labelAcademicBody;
    @FXML
    private TextFlow textInvestigationProjectName;
    @FXML
    private Label labelLGAC;
    @FXML
    private TextFlow textInvestigationLine;
    @FXML
    private Label labelDuration;
    @FXML
    private TextFlow textModalityRW;
    @FXML
    private TextFlow textReceptionWorkName;
    @FXML
    private TextFlow textRequisites;
    @FXML
    private Label labelDirector;
    @FXML
    private Label labelCodirector;
    @FXML
    private Label labelStudents;
    @FXML
    private TextFlow textInvestigationDescription;
    @FXML
    private TextFlow textReceptionWorkDescription;
    @FXML
    private TextFlow textExpectedResults;
    @FXML
    private TextFlow textBibliography;

    public String getReceptionWorkName() {
        return TransferProject.getReceptionWorkName();
    }
    
    public void getDetailedProject() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        DetailedProject detailedProject = (projectDAO.getProjectInfo(getReceptionWorkName()));
        
        labelAcademicBody.setText(detailedProject.getAcademicBodyName());
        
        Text investigationProjectName = new Text(detailedProject.getInvestigationProjectName());
        textInvestigationProjectName.getChildren().add(investigationProjectName);
        
        labelLGAC.setText(detailedProject.getLgacDescription());
        
        Text investigationLine = new Text(detailedProject.getInvestigationLine());
        textInvestigationLine.getChildren().add(investigationLine);
        
        labelDuration.setText(detailedProject.getApproxDuration());
        
        Text modalityRW = new Text(detailedProject.getReceptionWorkModality());
        textModalityRW.getChildren().add(modalityRW);
        
        Text receptionWorkName = new Text(detailedProject.getReceptionWorkName());
        textReceptionWorkName.getChildren().add(receptionWorkName);
        
        Text requisites = new Text(detailedProject.getRequisites());
        textRequisites.getChildren().add(requisites);
        
        labelDirector.setText(detailedProject.getDirector());
        labelCodirector.setText(detailedProject.getCoDirector());
        labelStudents.setText(String.valueOf(detailedProject.getNumberStudents()));
        
        Text investigationDescription = new Text(detailedProject.getInvestigationDescription());
        textInvestigationDescription.getChildren().add(investigationDescription);
        
        Text receptionWorkDescription = new Text(detailedProject.getReceptionWorkDescription());
        textReceptionWorkDescription.getChildren().add(receptionWorkDescription);
        
        Text expectedResults = new Text(detailedProject.getExpectedResults());
        textExpectedResults.getChildren().add(expectedResults);
        
        Text bibliography = new Text(detailedProject.getBibliography());
        textBibliography.getChildren().add(bibliography);
    }

    private String getTextWorkReceptionName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : textReceptionWorkName.getChildren()) {
            if (node instanceof Text) {
                Text textWorkReceptionName = (Text) node;
                stringBuilder.append(textWorkReceptionName.getText());
            }
        }
        return stringBuilder.toString();
    }

    private boolean confirmedDeleteProject() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar este Proyecto para siempre?");
        return response.get() == DialogGenerator.BUTTON_YES;
    }

    @FXML
    public void deleteProject() {
        if (confirmedDeleteProject()) {
            ProjectDAO projectDAO = new ProjectDAO();
            try {
                projectDAO.deleteProjectByTitle(getTextWorkReceptionName());
            } catch (SQLException deleteException) {
                deleteException.printStackTrace();
            }
            try {
                actionProjects();
            } catch (IOException changeException) {
                changeException.printStackTrace();
            }
        }
    }

    public void initialize() throws SQLException {
        getDetailedProject();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    public void actionProjects() throws IOException {
        MainStage.changeView("viewprojectproposals-view.fxml",800,600);
    }
    
    public void actionProfessors() {
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    public void logOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
