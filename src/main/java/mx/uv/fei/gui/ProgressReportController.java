package mx.uv.fei.gui;

import com.itextpdf.io.image.ImageDataFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.logic.*;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.log4j.Logger;

public class ProgressReportController implements IProfessorNavigationBar{
    @FXML
    private VBox vboxReportFormat;
    @FXML
    private HBox hboxLogOutLabel;
    @FXML
    private Label labelHeaderDate;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelStudent;
    @FXML
    private Label labelDirectors;
    @FXML
    private Label labelReceptionWork;
    @FXML
    private Label labelDate;
    @FXML
    private TableView<Evidence> tableViewEvidences;
    private TableColumn<Evidence, String> tableColumnProduct;
    private TableColumn<Evidence, String> tableColumnDeliverDate;
    private TableColumn<Evidence, String> tableColumnWasDelivered;
    LocalDate actualDate = LocalDate.now();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger logger = Logger.getLogger(ProgressReportController.class);
    
    public void initialize() {
        labelUsername.setText(SessionDetails.getInstance().getUsername());
        prepareTableViewEvidences();
        try {
            setInfoLabels();
            setTableHeight();
            fillTableViewEvidences();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "No se pudo recuperar la información.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
        labelDate.setText(actualDate.format(dateFormat));
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private void setInfoLabels() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        labelHeaderDate.setText(actualDate
                .getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, new Locale("es")) + " " + actualDate.getYear());
        labelStudent.setText(TransferStudent.getStudentName());
        labelReceptionWork.setText(TransferProject.getReceptionWorkName());
        labelDirectors.setText(professorDAO.getDirectorsByProject(TransferProject.getProjectID()));
    }
    
    private void prepareTableViewEvidences() {
        tableViewEvidences.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableColumnProduct = new TableColumn<>("Actividades/Productos");
        tableColumnProduct.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        tableColumnProduct.setResizable(false);
        tableColumnProduct.setMaxWidth(470);
        tableColumnProduct.setMinWidth(470);
        
        tableColumnDeliverDate = new TableColumn<>("Fecha de realización");
        tableColumnDeliverDate.setCellValueFactory((new PropertyValueFactory<>("deliverDate")));
        tableColumnDeliverDate.setResizable(false);
        tableColumnDeliverDate.setMaxWidth(135);
        tableColumnDeliverDate.setMinWidth(135);
        
        tableColumnWasDelivered = new TableColumn<>("¿Entregado en tiempo y forma?");
        tableColumnWasDelivered.setResizable(false);
        tableColumnWasDelivered.setMaxWidth(211);
        tableColumnWasDelivered.setMinWidth(211);
        
        tableViewEvidences.getColumns().clear();
        tableViewEvidences.getColumns().addAll(tableColumnProduct,tableColumnDeliverDate,tableColumnWasDelivered);
    }
    
    private int getNumberOfEvidences() throws SQLException{
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        return evidenceDAO.getDeliveredEvidencesByStudentID(TransferStudent.getStudentID()).size();
    }
    
    private void setTableHeight() throws SQLException{
        double rowHeight = 24.0;
        double headerHeight = 28.0;
        double minHeight;
        
        if (getNumberOfEvidences() == 0) {
            minHeight = rowHeight * 5;
        } else {
            minHeight = headerHeight + (getNumberOfEvidences() * rowHeight);
        }
        tableViewEvidences.setMinHeight(minHeight);
        tableViewEvidences.setMaxHeight(minHeight);
        tableViewEvidences.setPrefHeight(minHeight);
        
    }
    
    private void fillTableViewEvidences() throws SQLException{
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String studentID = TransferStudent.getStudentID();
        tableViewEvidences.getItems().clear();
        tableViewEvidences.getItems().addAll(evidenceDAO.getDeliveredEvidencesByStudentID(studentID));
    }

    @FXML
    private void generatePDF() {
        try {
            pdfGenerator();
        } catch (IOException ioException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Ocurrió un error al terminar de generar el PDF", AlertStatus.ERROR));
            logger.error(ioException);
        }
    }

    private void pdfGenerator() throws IOException {
        PdfWriter pdfWriter = null;
        PdfDocument pdfDocument = null;
        Document document = null;
        String documentName = null;

        try {
            documentName = "Reporte_" + TransferStudent.getStudentID() + "_" + actualDate + ".pdf";
            pdfWriter = new PdfWriter(new FileOutputStream( documentName));
            pdfDocument = new PdfDocument(pdfWriter);
            document = new Document(pdfDocument);
            
            WritableImage writableImage = vboxReportFormat.snapshot(null,null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            Image pdfImage = new Image(ImageDataFactory.create(bufferedImage,null));
            document.add(pdfImage);

        } catch (FileNotFoundException fileNotFoundException) {
            DialogGenerator.getDialog(new AlertMessage("Error al generar el PDF", AlertStatus.ERROR));
            logger.error(fileNotFoundException);
        } catch (IOException ioException) {
            DialogGenerator.getDialog(new AlertMessage("Error al crear el PDF", AlertStatus.ERROR));
            logger.error(ioException);
        } finally {
            if (document != null) {
                File report = new File(System.getProperty("user.home")
                        +"/IdeaProjects/SSPGER/" + documentName);

                File fileToMove = new File(System.getProperty("user.home")+ "/Documents/");

                File fileToSave = new File(fileToMove.getParent(), documentName);

                if (fileToSave.exists()) {
                    fileToSave.delete();
                }

                Files.move(report.toPath(), fileToSave.toPath());
                fileToMove.renameTo(fileToSave);

                DialogGenerator.getDialog(new AlertMessage(
                        "Se ha generado el reporte, en la siguiente ruta: " + fileToSave.getAbsolutePath(),
                        AlertStatus.SUCCESS));
                document.close();
            }
            if (pdfDocument != null) {
                pdfDocument.close();
            }
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_REPRESENTATIVE)) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_PROFESSOR)){
            MainStage.changeView(
                    "professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
    }
    
    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
