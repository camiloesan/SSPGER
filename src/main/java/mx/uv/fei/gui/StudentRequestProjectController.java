package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.ProjectRequest;
import mx.uv.fei.logic.TransferProject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class StudentRequestProjectController implements IStudentNavigationBar {
    @FXML
    private Label labelProjectTitle;
    @FXML
    private TextArea textAreaDescription;

    private boolean confirmedRequestProject() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea solicitar este proyecto?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    private ProjectRequest getProjectRequestAttributes() {
        ProjectRequest projectRequest = new ProjectRequest();

        projectRequest.setProjectID(TransferProject.getProjectID());
        projectRequest.setStudentId(LoginController.sessionDetails.getId());
        System.out.println();
        projectRequest.setDescription(textAreaDescription.getText());

        return projectRequest;
    }

    @FXML
    private void requestProject() {
        if (confirmedRequestProject()) {
            ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
            try {
                projectRequestDAO.createProjectRequest(getProjectRequestAttributes());
            } catch (SQLException requestProjectException) {
                requestProjectException.printStackTrace();
            }
        }
    }

    @Override
    public void redirectToAdvancements() throws IOException {

    }

    @Override
    public void redirectToEvidences() throws IOException, SQLException {

    }

    @Override
    public void redirectToProjects() throws IOException {

    }

    @Override
    public void redirectToRequest() {

    }

    @Override
    public void actionLogOut() throws IOException {

    }

    public void initialize() {
        labelProjectTitle.setText("Proyecto a solicitar: " + TransferProject.getReceptionWorkName());
    }
}
