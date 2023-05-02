package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class TimelineController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private void initialize() {

    }

    private void generateTimeline() {
        GridPane gridPane = new GridPane();
        ColumnConstraints colConstraint = new ColumnConstraints(120);
        colConstraint.setHalignment(HPos.LEFT);
        RowConstraints rowConstraints = new RowConstraints(130);
        rowConstraints.setValignment(VPos.CENTER);

    }
}
