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
import mx.uv.fei.logic.TransferStudent;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.Evidence;
import mx.uv.fei.logic.AlertStatus;
import mx.uv.fei.logic.AlertMessage;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
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
    private final String outputPath = System.getProperty("use.home") + "/Documents/Reportes/";
    
    private static final Logger logger = Logger.getLogger(ProjectRequestsController.class);
    
    public void initialize() {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        prepareTableViewEvidences();
        try {
            setInfoLabels();
            setTableHeight();
            fillTableViewEvidences();
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuperar la información.", AlertStatus.ERROR));
            logger.error(sqlException);
        }
        labelDate.setText(actualDate.format(dateFormat));
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private void setInfoLabels() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        labelHeaderDate.setText(actualDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("es")) + " " + actualDate.getYear());
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
        double minHeight = headerHeight + (getNumberOfEvidences() * rowHeight);
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
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al terminar de generar el PDF", AlertStatus.ERROR));
            logger.error(ioException);
        }
    }

    private void pdfGenerator() throws IOException {
        PdfWriter pdfWriter = null;
        PdfDocument pdfDocument = null;
        Document document = null;
        
        try{
            String documentName = "Reporte_" + TransferStudent.getStudentID() + "_" + actualDate  + ".pdf" ;
            pdfWriter = new PdfWriter(new FileOutputStream( documentName));
            pdfDocument = new PdfDocument(pdfWriter);
            document = new Document(pdfDocument);
            
            WritableImage writableImage = vboxReportFormat.snapshot(null,null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            Image pdfImage = new Image(ImageDataFactory.create(bufferedImage,null));
            document.add(pdfImage);
            
            DialogGenerator.getDialog(new AlertMessage("Se ha generado el reporte", AlertStatus.SUCCESS));
        } catch (FileNotFoundException fileNotFoundException) {
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al generar el PDF", AlertStatus.ERROR));
            logger.error(fileNotFoundException);
        } catch (IOException ioException) {
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al crear el PDF", AlertStatus.ERROR));
            logger.error(ioException);
        } finally {
            if (document != null) {
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
        if (Objects.equals(LoginController.sessionDetails.getUserType(), "RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else if (Objects.equals(LoginController.sessionDetails.getUserType(), "Profesor")){
            MainStage.changeView("professorviewprojects-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
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
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @Override public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            try {
                MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
