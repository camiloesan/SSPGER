package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentRequestProjectController implements IStudentNavigationBar {
    @FXML
    private Label labelProjectTitle;
    @FXML
    private TextArea textAreaDescription;
    @FXML
    private Label labelUsername;
    @FXML
    private HBox hboxLogOutLabel;
    private static final Logger logger = Logger.getLogger(StudentRequestProjectController.class);
    private static final int DESCRIPTION_PROJECT_REQUEST_MAX_LENGTH = 850;
    
    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        labelProjectTitle.setText("Proyecto a solicitar: " + TransferProject.getReceptionWorkName());
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    private boolean confirmedRequestProject() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea solicitar este proyecto?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    private ProjectRequest getProjectRequestAttributes() {
        ProjectRequest projectRequest = new ProjectRequest();

        projectRequest.setProjectID(TransferProject.getProjectID());
        projectRequest.setStudentId(LoginController.sessionDetails.getId());
        projectRequest.setDescription(textAreaDescription.getText());

        return projectRequest;
    }

    private boolean confirmedFields() {
        boolean result;
        if (textAreaDescription.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Debe ingresar los motivos de la solicitud", AlertStatus.WARNING));
            result = false;
        } else {
            if (textAreaDescription.getText().length() > DESCRIPTION_PROJECT_REQUEST_MAX_LENGTH) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Los motivos exceden el límite de caracteres: " + DESCRIPTION_PROJECT_REQUEST_MAX_LENGTH, AlertStatus.WARNING));
            }
            result = true;
        }

        return result;
    }

    private boolean confirmedRequests() throws SQLException{
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        boolean result;
        int requests = projectRequestDAO.getProjectRequestsByStudentID(SessionDetails.getInstance().getId());
        if (requests>0) {
            DialogGenerator.getDialog(new AlertMessage("Ya haz solicitado un proyecto",
                    AlertStatus.WARNING));
            result = false;
        } else {
            result = true;
        }
        return result;
    }
    
    private boolean projectHasSpaces() {
        boolean flag = false;
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            boolean projectOutOfSpaces = projectDAO.projectOutOfSpaces(TransferProject.getProjectID());
            if (!projectOutOfSpaces) {
                flag = true;
            } else {
                DialogGenerator.getDialog(new AlertMessage("Ya no hay espacios disponibles para este proyecto", AlertStatus.WARNING));
            }
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("Error al comprobar los espacios disponibles en el proyecto.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
        return flag;
    }

    @FXML
    private void requestProject() throws IOException {
        try {
            if (confirmedRequests() && confirmedFields() && confirmedRequestProject() && projectHasSpaces()) {
                ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
                try {
                    projectRequestDAO.createProjectRequest(getProjectRequestAttributes());
                } catch (SQLException requestProjectException) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "Sin conexión a la base de datos, no se pudo crear la petición", AlertStatus.ERROR));
                    logger.error(requestProjectException);
                }
                redirectToProjects();
            }
        } catch (SQLException getRequestsException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar el número de peticiones", AlertStatus.ERROR));
            logger.error(getRequestsException);
        }
    }

    @Override
    public void redirectToAdvancements() throws IOException {
        MainStage.changeView("studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToEvidences() throws IOException, SQLException {
        MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToProjects() throws IOException {
        MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void redirectToRequest() throws IOException {
        MainStage.changeView("studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }

    private boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
}
