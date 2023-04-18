package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.EvidenceDAO;

import java.io.File;
import java.sql.SQLException;

public class AddEvidenceController {
    @FXML
    private Button addFileButton;

    @FXML
    private void onActionAddFileButton(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        fileChooser.setInitialDirectory(new File("/home"));
        File evidence = fileChooser.showOpenDialog(new Stage());

    }
}
