package mx.uv.fei.gui;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        setInfoLabels();
        prepareTableViewEvidences();
        fillTableViewEvidences();
        setTableHeight();
        labelDate.setText(actualDate.format(dateFormat));
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private void setInfoLabels() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        labelHeaderDate.setText(actualDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("es")) + " " + actualDate.getYear());
        labelStudent.setText(TransferStudent.getStudentName());
        labelDirectors.setText(professorDAO.getProfessorsByProject(TransferProject.getProjectID()));
        labelReceptionWork.setText(TransferProject.getReceptionWorkName());
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
    
    private int getNumberOfEvidences() {
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        return evidenceDAO.getDeliveredEvidences(TransferStudent.getStudentID()).size();
    }
    
    private void setTableHeight() {
        double rowHeight = 24.0;
        double headerHeight = 28.0;
        double minHeight = headerHeight + (getNumberOfEvidences() * rowHeight);
        tableViewEvidences.setMinHeight(minHeight);
        tableViewEvidences.setMaxHeight(minHeight);
        tableViewEvidences.setPrefHeight(minHeight);
    }
    
    private void fillTableViewEvidences(){
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidences.getItems().clear();
        tableViewEvidences.getItems().addAll(evidenceDAO.getDeliveredEvidences(TransferStudent.getStudentID()));
    }
    
    @FXML
    private void generateProgressReport() throws IOException {
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
            fileNotFoundException.printStackTrace();
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al generar el PDF", AlertStatus.ERROR));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al generar el PDF", AlertStatus.ERROR));
            System.out.println(ioException.getMessage());
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
    
    @FXML
    private void generateProgressReportBOX() {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        
        try {
            WritableImage image = vboxReportFormat.snapshot(null,null);
            
            PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, SwingFXUtils.fromFXImage(image,null));
            PDPageContentStream contentStream = new PDPageContentStream(document,page);
            contentStream.drawImage(pdImageXObject,0,0);
            contentStream.close();
            
            document.save("Reporte_" + TransferStudent.getStudentName() + "_" + actualDate);
            document.close();
            
            DialogGenerator.getDialog(new AlertMessage("Documento guardado",AlertStatus.SUCCESS));
            
        } catch (IOException ioException) {
            ioException.printStackTrace();
            DialogGenerator.getDialog(new AlertMessage("Ocurrió un error al generar el documento.", AlertStatus.ERROR));
        }
    }
    
    @Override
    public void redirectToProfessorAdvancementManagement() throws IOException {
        try {
            MainStage.changeView("advancementsmanagement-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorProjectManagement() throws IOException {
        try {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProfessorEvidenceManager() throws IOException {
        try {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    @Override
    public void redirectToProjectRequests() throws IOException {
        try {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
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
