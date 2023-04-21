package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import mx.uv.fei.dao.AdvancementDAO;
import mx.uv.fei.logic.Advancement;

import java.io.IOException;
import java.sql.SQLException;

public class CRUDEvidenceController {
    @FXML
    private ListView<String> listViewAdvancementTitle;

    @FXML
    private void buttonAddEvidence() throws IOException {
        AddEvidenceWindow addEvidenceWindow = new AddEvidenceWindow();
        Stage mainStage = (Stage) listViewAdvancementTitle.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        addEvidenceWindow.start(subStage);
    }

    @FXML
    private void buttonModifyEvidence() throws IOException {
        ModifyEvidenceWindow modifyEvidenceWindow = new ModifyEvidenceWindow();
        Stage mainStage = (Stage) listViewAdvancementTitle.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        modifyEvidenceWindow.start(subStage);
    }

    @FXML
    private void buttonDeleteEvidence() throws IOException {
        DeleteEvidenceWindow deleteEvidenceWindow = new DeleteEvidenceWindow();
        Stage mainStage = (Stage) listViewAdvancementTitle.getScene().getWindow();
        Stage subStage = new Stage();
        subStage.initOwner(mainStage);
        deleteEvidenceWindow.start(subStage);
    }

    @FXML
    private void updateListView() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        listViewAdvancementTitle.getItems().clear();
        for(Advancement advancementObject : advancementDAO.getListAdvancementName()) {
            listViewAdvancementTitle.getItems().add(advancementObject.getAdvancementName());
        }
    }
    @FXML
    private void initialize() throws SQLException {
        updateListView();
    }
}
