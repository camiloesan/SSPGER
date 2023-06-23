package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentProjectRequestDetailsController implements IStudentNavigationBar {
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelReceptionWorkName;
    @FXML
    private Label labelStateProjectRequest;
    @FXML
    private Label labelDescriptionProjectRequest;
    @FXML
    private GridPane gridPaneProjectRequestData;
    @FXML
    private Button buttonDelete;
    private static final Logger logger = Logger.getLogger(StudentProjectRequestDetailsController.class);

    @FXML
    private void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        showRequestInfo();
    }
    
    public void showRequestInfo() {
        try {
            getProjectRequestInfo();
            checkRequestState();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar la información de las peticiones",
                    AlertStatus.ERROR));
        }
    }
    
    private void checkRequestState() throws SQLException{
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        if (projectRequestDAO.isRequestApproved(SessionDetails.getInstance().getId())) {
            buttonDelete.setVisible(false);
        }
    }

    private void getProjectRequestInfo() throws SQLException{
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        ProjectRequest projectRequest = projectRequestDAO.getProjectRequestInfoByStudentID(SessionDetails.getInstance()
                .getId());
        labelReceptionWorkName.setText(projectRequest.getProjectName());
        labelStateProjectRequest.setText(projectRequest.getStatus());
        labelDescriptionProjectRequest.setText(projectRequest.getDescription());
    }

    private boolean existsProjectRequests() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        int projectrequests = 0;
        boolean result;
        try {
            projectrequests = projectRequestDAO
                    .getProjectRequestsByStudentID(SessionDetails.getInstance().getId());
        } catch (SQLException projectRequestsException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No hay conexión a la base de datos, no se pudo recuperar el número de peticiones",
                    AlertStatus.ERROR));
            logger.error(projectRequestsException);
        }

        if (projectrequests == 0) {
            gridPaneProjectRequestData.setVisible(false);
            DialogGenerator.getDialog(new AlertMessage("Aún no existen peticiones", AlertStatus.WARNING));
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    @FXML
    private void deleteProjectRequest() throws IOException {
        if (confirmedDelete()) {
            ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();

            try {
                if (projectRequestDAO.deleteProjectRequest(projectRequestDAO.getProjectRequestIDByStudentID(
                        SessionDetails.getInstance().getId())) == 1) {
                    DialogGenerator.getDialog(new AlertMessage(
                            "Se eliminó con éxito la petición", AlertStatus.SUCCESS));
                    redirectToProjects();
                }
            } catch (SQLException deleteProjectRequestException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "No hay conexión a la base de datos, no se pudo eliminar la petición",
                        AlertStatus.ERROR));
                logger.error(deleteProjectRequestException);
            }
        }
    }

    private boolean confirmedDelete() {
        Optional<ButtonType> response = DialogGenerator
                .getConfirmationDialog("¿Está seguro que desea eliminar la petición?");
        return (response.get() == DialogGenerator.BUTTON_YES);
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
        MainStage.changeView("studentprojectrequestdetails-view.fxml", 1000, 600 +
                MainStage.HEIGHT_OFFSET);
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
