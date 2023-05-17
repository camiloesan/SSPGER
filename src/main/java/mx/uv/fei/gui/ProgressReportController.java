package mx.uv.fei.gui;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.logic.Evidence;
import mx.uv.fei.logic.TransferProject;
import mx.uv.fei.logic.TransferStudent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProgressReportController implements IProfessorNavigationBar{
    @FXML
    private HBox hboxLogOutLabel;
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
    
    public void initialize() throws SQLException {
        labelUsername.setText(LoginController.sessionDetails.getUsername());
        setNamesLabels();
        prepareTableViewEvidences();
        fillTableViewEvidences();
        labelDate.setText(actualDate.format(dateFormat));
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }
    
    private void setNamesLabels() throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        labelStudent.setText(TransferStudent.getStudentName());
        labelDirectors.setText(professorDAO.getProfessorsByProject(TransferProject.getProjectID()));
        labelReceptionWork.setText(TransferProject.getReceptionWorkName());
    }
    
    private void prepareTableViewEvidences() {
        double rowHeight = 24.0;
        double headerHeight = 24.0;
        double minHeight = headerHeight + tableViewEvidences.getItems().size() * rowHeight;
        
        tableViewEvidences.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //tableViewEvidences.setPrefHeight(minHeight);
        //tableViewEvidences.prefHeightProperty().bind(Bindings.max(minHeight,tableViewEvidences.minHeightProperty()));
        //tableViewEvidences.setMaxHeight((tableViewEvidences.getItems().size() * rowHeight) + headerHeight);
        //.setMinHeight((tableViewEvidences.getItems().size() * rowHeight) + headerHeight);
        //tableViewEvidences.prefHeightProperty().bind(Bindings.size(tableViewEvidences.getItems()).multiply(rowHeight).add(headerHeight));
        
        tableColumnProduct = new TableColumn<>("Actividades/Productos");
        tableColumnProduct.setCellValueFactory(new PropertyValueFactory<>("evidenceTitle"));
        
        tableColumnDeliverDate = new TableColumn<>("Fecha de realización");
        tableColumnDeliverDate.setCellValueFactory((new PropertyValueFactory<>("deliverDate")));
        
        tableColumnWasDelivered = new TableColumn<>("¿Entregado en tiempo y forma?");
        
        tableViewEvidences.getColumns().clear();
        tableViewEvidences.getColumns().addAll(tableColumnProduct,tableColumnDeliverDate,tableColumnWasDelivered);
    }
    
    private void fillTableViewEvidences(){
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        tableViewEvidences.getItems().clear();
        tableViewEvidences.getItems().addAll(evidenceDAO.getDeliveredEvidences(TransferStudent.getStudentID()));
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
