package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import mx.uv.fei.dao.AdvancementDAO;
import mx.uv.fei.logic.Advancement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdvancementsManagementController implements IProfessorNavigationBar {
    @FXML
    private TextField advancementToModify;
    @FXML
    private DatePicker newAdvancementDeadline;
    @FXML
    private TextArea newAdvancementDescription;
    @FXML
    private TextField newAdvancementName;
    @FXML
    private DatePicker newAdvancementStartDate;
    @FXML
    private ComboBox<String> newProjectToAssign;
    @FXML
    private DatePicker advancementDeadline;
    @FXML
    private TextArea advancementDescription;
    @FXML
    private TextField advancementName;
    @FXML
    private DatePicker advancementStartDate;
    @FXML
    private ComboBox<String> projectToAssign;

    @FXML
    private void scheduleAdvancementButtonAction() throws SQLException {
        scheduleAdvancement();
    }

    private void scheduleAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement = new Advancement();
        advancement.setAdvancementName(advancementName.getText());
        advancement.setAdvancementStartDate(String.valueOf(advancementStartDate.getValue()));
        advancement.setAdvancementDeadline(String.valueOf(advancementDeadline.getValue()));
        //advancement.setProjectId();
        advancement.setProfessorId(advancementDAO.getProfessorIdByUsername(LoginController.sessionDetails.getUsername()));
        advancement.setAdvancementDescription(advancementDescription.getText());
        advancementDAO.addAdvancement(advancement);
    }

    @FXML
    private void modifyAdvancementButtonAction() {
        modifyAdvancement();
    }

    private void modifyAdvancement() {

    }

    @FXML
    private void initialize() {
        System.out.println(LoginController.sessionDetails.getUsername());
    }

    @Override
    public void redirectToAdvancementManagement() {

    }

    @Override
    public void redirectToProjectManagement() {

    }

    @Override
    public void redirectToEvidences() {

    }

    @Override
    public void redirectToRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 800, 500 + MainStage.HEIGHT_OFFSET);
    }

    @Override
    public void actionLogOut() throws IOException {
        LoginController.sessionDetails.cleanSessionDetails();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Está seguro que desea salir, se cerrará su sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty() || result.get() != ButtonType.OK) {
            alert.close();
        } else {
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
