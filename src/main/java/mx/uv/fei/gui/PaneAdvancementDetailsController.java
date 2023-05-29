package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.AlertMessage;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.TransferAdvancement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PaneAdvancementDetailsController {
    @FXML
    private Label labelAdvancementName;
    @FXML
    private TextFlow textAdvancementDescription;
    @FXML
    private Label labelStartDate;
    @FXML
    private Label labelDeadline;
    
    public void initialize() throws SQLException {
        getDetailedAdvancement();
    }
    
    public String getAdvancementName() {
        return TransferAdvancement.getAdvancementName();
    }
    
    public void getDetailedAdvancement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement detaildedAdvancement = (advancementDAO.getAdvancementDetailById(TransferAdvancement.getAdvancementID()));

        labelAdvancementName.setText(detaildedAdvancement.getAdvancementName());
        
        Text advancementDescription = new Text(detaildedAdvancement.getAdvancementDescription());
        textAdvancementDescription.getChildren().add(advancementDescription);
        
        labelStartDate.setText(detaildedAdvancement.getAdvancementStartDate());

        labelDeadline.setText(detaildedAdvancement.getAdvancementDeadline());
    }
    
    @FXML
    public void returnToAdvancementList() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }
    
    public void deleteAdvancement(){
        AdvancementDAO advancementDAO = new AdvancementDAO();
        try {
            advancementDAO.deleteAdvancementById(TransferAdvancement.getAdvancementID());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo eliminar el avance, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }
    
    public void deleteAdvancementButtonAction() {
        if (confirmedDeletion()) {
            deleteAdvancement();
        }
    }
    
    public boolean confirmedDeletion() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar el avance \"" + getAdvancementName() + "\"?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }
}
