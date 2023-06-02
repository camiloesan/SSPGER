package mx.uv.fei.gui;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


public class TimelineController {
    @FXML
    private ScrollPane scrollPaneTimeline;
    @FXML
    private Label labelTitle;
    private static final Logger logger = Logger.getLogger(TimelineController.class);

    @FXML
    private void initialize() {
        String projectName;
        projectName = TransferProject.getReceptionWorkName();
        labelTitle.setText("Cronograma de avances [" +  projectName + "]");
        try {
            generateTimeline();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo recuperar la información de la base de datos", AlertStatus.ERROR));
            logger.error(sqlException);
        }
    }

    private void generateTimeline() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        List<Advancement> advancementList = advancementDAO
                .getAdvancementListByProjectId(TransferProject.getProjectID());

        AnchorPane anchorPane = new AnchorPane();

        int labelDatesX = 260;
        int labelProjectNameX = 30;
        int labelProjectNameY = 100;

        Label labelAdvancements = new Label();
        labelAdvancements.setText("NOMBRE DEL AVANCE");
        labelAdvancements.setLayoutX(30);
        labelAdvancements.setLayoutY(50);
        labelAdvancements.setFont(Font.font("System" ,FontWeight.BOLD, 14));
        anchorPane.getChildren().add(labelAdvancements);

        Label todayDate = new Label();
        todayDate.setText(LocalDate.now().toString());
        todayDate.setLayoutX(225);
        todayDate.setLayoutY(20);
        todayDate.setTextFill(Color.web("#61192C"));
        anchorPane.getChildren().add(todayDate);

        Line todayDateLine = new Line();
        todayDateLine.setStartX(labelDatesX);
        todayDateLine.setStartY(50);
        todayDateLine.setEndX(labelDatesX);
        todayDateLine.setEndY(labelProjectNameY + (advancementList.size() * 70));
        todayDateLine.setStroke(Color.web("#61192C"));
        anchorPane.getChildren().add(todayDateLine);

        LocalDate today = LocalDate.now();
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        long daysUntilNextMonth = ChronoUnit.DAYS.between(today, endOfMonth);
        long monthLineStartX = labelDatesX + (daysUntilNextMonth * 4);
        for (int j = 0; j < 12; j++) {
            Line monthLine = new Line();
            monthLine.setStartX(monthLineStartX);
            monthLine.setStartY(50);
            monthLine.setEndX(monthLineStartX);
            monthLine.setEndY(labelProjectNameY + (advancementList.size() * 70));
            monthLine.setStroke(Color.GRAY);
            anchorPane.getChildren().add(monthLine);
            monthLineStartX+=30*4;
        }

        int i = 0;
        for (Advancement advancementObject : advancementList) {
            i++;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateStart = LocalDate.parse(advancementObject.getAdvancementStartDate(),
                    dateTimeFormatter).atStartOfDay();
            LocalDateTime dateEnd = LocalDate.parse(advancementObject.getAdvancementDeadline(),
                    dateTimeFormatter).atStartOfDay();
            long daysBetween = Duration.between(dateStart, dateEnd).toDays();
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
            advancementRectangle.setLayoutX(labelDatesX + (offsetDays * 4));
            advancementRectangle.setLayoutY(labelProjectNameY-5);
            advancementRectangle.setViewOrder(-1.0);
            advancementRectangle.setCursor(Cursor.CLOSED_HAND);

            var scaleTrans = new ScaleTransition(javafx.util.Duration.millis(250), advancementRectangle);
            scaleTrans.setFromX(1.0);
            scaleTrans.setFromY(1.0);
            scaleTrans.setToX(1.2);
            scaleTrans.setToY(1.2);
            advancementRectangle.setOnMouseEntered(mouseEvent -> {
                scaleTrans.setRate(1.0);
                advancementRectangle.setViewOrder(-1.0);
                scaleTrans.play();
            });
            advancementRectangle.setOnMouseExited(mouseEvent -> {
                scaleTrans.setRate(-1.0);
                advancementRectangle.setViewOrder(0.0);
                scaleTrans.play();
            });
            advancementRectangle.setOnMouseClicked(mouseEvent -> {
                TransferAdvancement.setAdvancementName(advancementObject.getAdvancementName());
                TransferAdvancement.setAdvancementID(advancementObject.getAdvancementID());
                if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_STUDENT)) {
                    try {
                        MainStage.changeView("studentviewadvancementdetails-view.fxml", 1000, 600 +
                                MainStage.HEIGHT_OFFSET);
                    } catch (IOException ioException) {
                        logger.error(ioException);
                    }
                } else {
                    try {
                        MainStage.changeView("paneadvancementdetails-view.fxml", 1000, 600 +
                                MainStage.HEIGHT_OFFSET);
                    } catch (IOException ioException) {
                        logger.error(ioException);
                    }
                }
            });

            anchorPane.getChildren().add(advancementRectangle);
            labelProjectNameY += 70;
        }

        scrollPaneTimeline.setContent(anchorPane);
    }

    @FXML
    private void returnToPreviousWindow() throws IOException {
        switch (SessionDetails.getInstance().getUserType()) {
            case "Profesor" -> MainStage.changeView(
                    "professorviewprojects-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            case "RepresentanteCA" -> MainStage.changeView(
                    "projectproposals-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
            case "Estudiante" -> MainStage.changeView(
                    "studentviewprojects-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }
}
