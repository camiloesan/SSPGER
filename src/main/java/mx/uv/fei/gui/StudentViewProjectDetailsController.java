package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.SessionDetails;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentViewProjectDetailsController implements IStudentNavigationBar {
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
    private Label labelUsername;
    @FXML
    private TextFlow textInvestigationDescription;
    @FXML
    private TextFlow textReceptionWorkDescription;
    @FXML
    private TextFlow textExpectedResults;
    @FXML
    private TextFlow textBibliography;
    private int projectID;
    private static final Logger logger = Logger.getLogger(StudentViewProjectDetailsController.class);
    
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        showProjectDetails();
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private void showProjectDetails() {
        try {
            getDetailedProject();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar la información del proyecto.",
                    AlertStatus.ERROR));
        }
    }
    
    private void getDetailedProject() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        DetailedProject detailedProject = (projectDAO.getProjectInfoByID(TransferProject.getProjectID()));

        projectID = detailedProject.getProjectID();
        
        labelAcademicBody.setText(detailedProject.getAcademicBodyName());
        
        Text investigationProjectName = new Text(detailedProject.getInvestigationProjectName());
        textInvestigationProjectName.getChildren().add(investigationProjectName);
        
        labelLGAC.setText(detailedProject.getLgacName());
        
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
            if (node instanceof Text textWorkReceptionName) {
                stringBuilder.append(textWorkReceptionName.getText());
            }
        }
        return stringBuilder.toString();
    }
    
    private boolean projectHasSpaces() throws SQLException {
        ProjectDAO projectDAO = new ProjectDAO();
        boolean flag;
        
        boolean projectOutOfSpaces = projectDAO.projectOutOfSpaces(TransferProject.getProjectID());
        flag = !projectOutOfSpaces;
        return flag;
    }
    
    private boolean studentAlreadyRequested() throws SQLException {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        boolean flag = true;
        int requests = projectRequestDAO.getProjectRequestsByStudentID(SessionDetails.getInstance().getId());
        if (requests == 0) {
            flag = false;
        }
        return flag;
    }

    @FXML
    private void redirectToProjectRequest() throws IOException {
        try {
            if (studentAlreadyRequested()) {
                DialogGenerator.getDialog(new AlertMessage("Ya tienes una solicitud.", AlertStatus.WARNING));
            } else if (projectHasSpaces()) {
                TransferProject.setReceptionWorkName(getTextWorkReceptionName());
                MainStage.changeView("studentrequestproject-view.fxml", 1000, 600 +
                        MainStage.HEIGHT_OFFSET);
            } else {
                DialogGenerator.getDialog(new AlertMessage("Ya no hay espacios disponibles para este proyecto",
                        AlertStatus.WARNING));
            }
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("Sin conexión a la base de datos, no se pudo " +
                    "comprobar los espacios disponibles.", AlertStatus.ERROR));
            logger.error(sqlException);
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
