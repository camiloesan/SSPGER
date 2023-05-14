package mx.uv.fei.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.Advancement;
import mx.uv.fei.logic.TransferProject;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimelineController {
    @FXML
    private ScrollPane scrollPaneTimeline;
    @FXML
    private Label labelTitle;

    @FXML
    private void initialize() throws SQLException {
        String projectName;
        projectName = TransferProject.getReceptionWorkName();
        labelTitle.setText("Cronograma de avances [" +  projectName + "]");
        generateTimeline();
    }

    private void generateTimeline() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        List<Advancement> advancementList = advancementDAO.getAdvancementListByProjectId(TransferProject.getProjectID());

        int labelDatesX = 220;
        int labelProjectNameX = 30;
        int labelProjectNameY = 100;

        AnchorPane anchorPane = new AnchorPane();
        Line dateLine = new Line();
        dateLine.setStartX(labelDatesX);
        dateLine.setStartY(50);
        dateLine.setEndX(labelDatesX);
        dateLine.setEndY(labelProjectNameY + 350);
        dateLine.setStroke(Color.web("#61192C"));
        anchorPane.getChildren().add(dateLine);

        int i = 0;
        for (Advancement advancementObject : advancementList) {
            i++;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateStart = LocalDate.parse(advancementObject.getAdvancementStartDate(), dateTimeFormatter).atStartOfDay();
            LocalDateTime dateEnd = LocalDate.parse(advancementObject.getAdvancementDeadline(), dateTimeFormatter).atStartOfDay();
            long daysBetween = Duration.between(dateStart, dateEnd).toDays();
            //LocalDateTime testDate = LocalDate.parse("2023-05-29", dateTimeFormatter).atStartOfDay();
            long offsetDays = Duration.between(LocalDate.now().atStartOfDay(), dateStart).toDays();

            Label labelProjectName = new Label(advancementObject.getAdvancementName());
            labelProjectName.setLayoutX(labelProjectNameX);
            labelProjectName.setLayoutY(labelProjectNameY);
            anchorPane.getChildren().add(labelProjectName);

            Rectangle advancementRectangle = new Rectangle(daysBetween * 4, 30);
            if (i % 2 == 0) {
                advancementRectangle.setFill(Color.web("#D7A319"));
            } else {
                advancementRectangle.setFill(Color.web("#003466"));
            }
            advancementRectangle.setArcHeight(10);
            advancementRectangle.setArcWidth(10);
            advancementRectangle.setLayoutX(labelDatesX + offsetDays);
            advancementRectangle.setLayoutY(labelProjectNameY-5);
            anchorPane.getChildren().add(advancementRectangle);

            labelProjectNameY += 70;
        }
        scrollPaneTimeline.setContent(anchorPane);
    }

    @FXML
    private void returnToPreviousWindow() throws IOException {
        switch (LoginController.sessionDetails.getUserType()) {
            case "Profesor", "RepresentanteCA" -> MainStage.changeView("advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            case "Estudiante" -> MainStage.changeView("studentviewprojects-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }
}
