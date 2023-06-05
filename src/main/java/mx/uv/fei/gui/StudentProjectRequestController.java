package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentProjectRequestController implements IStudentNavigationBar {
    @FXML
    Label labelUsername;
    @FXML
    Label labelReceptionWorkName;
    @FXML
    Label labelStateProjectRequest;
    @FXML
    Label labelDescriptionProjectRequest;
    @FXML
    GridPane gridPaneProjectRequestData;
    private static final Logger logger = Logger.getLogger(StudentProjectRequestController.class);

    @FXML
    public void initialize() throws IOException {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        if(existsProjectRequests()) {
            getProjectRequestInfo();
        }
    }

    public void getProjectRequestInfo() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        ProjectRequest projectRequest = new ProjectRequest();
        try {
            projectRequest = projectRequestDAO
                    .getProjectRequestInfoByStudentID(SessionDetails.getInstance().getId());
        } catch (SQLException projectRequestInfoException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar información de la petición", AlertStatus.ERROR));
            logger.error(projectRequestInfoException);
        }
        labelReceptionWorkName.setText(projectRequest.getProjectName());
        labelStateProjectRequest.setText(projectRequest.getStatus());
        labelDescriptionProjectRequest.setText(projectRequest.getDescription());
    }

    public boolean existsProjectRequests() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        int projectrequests = 0;
        boolean result;
        try {
            projectrequests = projectRequestDAO
                    .getProjectRequestsByStudentID(SessionDetails.getInstance().getId());
        } catch (SQLException projectRequestsException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar número de peticiones", AlertStatus.ERROR));
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
    public void deleteProjectRequest() throws IOException {
        if (confirmedDelete()) {
            ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
            int result = 0;

            try {
                result = projectRequestDAO.deleteProjectRequest(projectRequestDAO
                        .getProjectRequestIDByStudentID(SessionDetails.getInstance().getId()));
                redirectToProjects();
            } catch (SQLException deleteProjectRequestException) {
                DialogGenerator.getDialog(new AlertMessage(
                        "Error al eliminar la información", AlertStatus.ERROR));
                logger.error(deleteProjectRequestException);
            }

            if (result == 1) {
                DialogGenerator.getDialog(new AlertMessage(
                                "Se eliminó con exito la petición", AlertStatus.SUCCESS));
            } else {
                DialogGenerator.getDialog(new AlertMessage(
                        "No se pudo modificar la petición", AlertStatus.WARNING));
            }
        }
    }

    public boolean confirmedDelete() {
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
        MainStage.changeView("studentprojectrequest-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
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
