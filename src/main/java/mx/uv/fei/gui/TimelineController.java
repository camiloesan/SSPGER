package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.Advancement;

import java.sql.SQLException;
import java.util.List;

public class TimelineController {
    @FXML
    private ScrollPane scrollPaneTimeline;
    @FXML
    private Label labelTitle;

    @FXML
    private void initialize() throws SQLException {
        String projectName;
        projectName = "Hacia un Modelo de Campus Accsesible: Facultad de Estadística e Informática";
        labelTitle.setText("Cronograma de avances [" +  projectName + "]");
        generateTimeline();
    }

    private void generateTimeline() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        List<Advancement> advancementList = advancementDAO.getAdvancementListByProjectName("Hacia un Modelo de Campus Accsesible: Facultad de Estadística e Informática");

        int labelDatesX = 150;
        int labelDatesY = 50;
        int labelProjectNameX = 20;
        int labelProjectNameY = 100;

        String textDates;
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(40, 20, 20, 40));
        for (Advancement advancementObject : advancementList) {
            textDates = advancementObject.getAdvancementStartDate() + "/" + advancementObject.getAdvancementDeadline();
            Label labelDate = new Label(textDates);
            labelDate.setLayoutX(labelDatesX);
            labelDate.setLayoutY(labelDatesY);
            anchorPane.getChildren().add(labelDate);

            Label labelProjectName = new Label(advancementObject.getAdvancementName());
            labelProjectName.setLayoutX(labelProjectNameX);
            labelProjectName.setLayoutY(labelProjectNameY);
            anchorPane.getChildren().add(labelProjectName);

            Rectangle rectangle = new Rectangle(170, 50, Color.RED);
            rectangle.setLayoutX(labelDatesX);
            rectangle.setLayoutY(labelProjectNameY-15);
            anchorPane.getChildren().add(rectangle);

            labelProjectNameY += 70;
            labelDatesX += 200;
        }
        scrollPaneTimeline.setContent(anchorPane);
    }
}
