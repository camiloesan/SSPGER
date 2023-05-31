package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.ProjectRequest;
import mx.uv.fei.logic.SessionDetails;

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

    @FXML
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        getProjectRequestInfo();
    }

    public void getProjectRequestInfo() {
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        ProjectRequest projectRequest = new ProjectRequest();
        int projectrequests = 0;

        try {
            projectrequests = projectRequestDAO
                    .getProjectRequestsByStudentID(SessionDetails.getInstance().getId());
            projectRequest = projectRequestDAO
                    .getProjectRequestInfoByStudentID(SessionDetails.getInstance().getId());
        } catch (SQLException projectRequestInfoException) {
            projectRequestInfoException.printStackTrace();
        }

        if (projectrequests == 0) {
            gridPaneProjectRequestData.setVisible(false);
            DialogGenerator.getDialog(new AlertMessage("Aún no existen peticiones", AlertStatus.WARNING));
        } else {
            labelReceptionWorkName.setText(projectRequest.getProjectName());
            labelStateProjectRequest.setText(projectRequest.getStatus());
            labelDescriptionProjectRequest.setText(projectRequest.getDescription());
        }
    }

    @FXML
    public void deleteProjectRequest() {
        if (confirmedDelete()) {
            ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
            try {
                projectRequestDAO.deleteProjectRequest(projectRequestDAO
                        .getProjectRequestIDByStudentID(SessionDetails.getInstance().getId()));
            } catch (SQLException deleteProjectRequestException) {
                deleteProjectRequestException.printStackTrace();
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
        MainStage.changeView("studentprojectrequestview.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
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
