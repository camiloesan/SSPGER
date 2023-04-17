package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AdvancementsTimelineController {
    @FXML
    Group group;

    private void draw() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(730);
        rectangle.setHeight(50);
        rectangle.setFill(Color.GRAY);
        rectangle.setCursor(Cursor.CLOSED_HAND);
        group.getChildren().add(rectangle);
    }

    @FXML
    private void initialize() {
        draw();
    }

}
