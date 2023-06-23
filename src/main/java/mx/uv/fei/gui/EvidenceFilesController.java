package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class EvidenceFilesController implements IStudentNavigationBar {
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelRequest;
    @FXML
    private TableView<File> tableViewFiles;
    @FXML
    private Button buttonAddFile;
    @FXML
    private Button buttonDeleteFile;
    @FXML
    private HBox hboxLogOutLabel;
    private static final Logger logger = Logger.getLogger(EvidenceFilesController.class);

    public void initialize() {

        if (getEvidenceDirectory().exists()) {

            fillTableViewFiles();
        }

        labelUsername.setText(SessionDetails.getInstance().getUsername());

        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_STUDENT)
                && !isAdvancementOverdue()) {
            labelRequest.setText("Mi Petición");
            buttonAddFile.setVisible(true);
            buttonDeleteFile.setVisible(true);
        }
        VBox.setVgrow(hboxLogOutLabel, Priority.ALWAYS);
    }

    private void fillTableViewFiles() {

        TableColumn<File, String> nameColumn = new TableColumn<>("Nombre del archivo");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableViewFiles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableViewFiles.getColumns().clear();
        tableViewFiles.getColumns().addAll(nameColumn);

        File folder = new File(getEvidenceDirectory().getAbsolutePath());
        File[] files = folder.listFiles();

        ObservableList<File> fileList = FXCollections.observableArrayList(files);

        tableViewFiles.getItems().clear();
        tableViewFiles.setItems(fileList);

    }

    private boolean isAdvancementOverdue() {
        boolean result = false;
        AdvancementDAO advancementDAO = new AdvancementDAO();
        LocalDate deadline;

        try {
            deadline = advancementDAO.getAdvancementDeadLineByEvidenceID(TransferEvidence.getEvidenceId());
            if (deadline.isBefore(LocalDate.now()) && deadline != null) {
                result = true;
            }
        } catch (SQLException deadlineException) {
            DialogGenerator.getDialog(new AlertMessage("No hay conexión a la base de datos, no se pudo comprobar" +
                    " la fecha de entrega.", AlertStatus.ERROR));
            logger.error(deadlineException);
        }

        return result;
    }

    @FXML
    private void visualizeFile() throws IOException {
        if (tableViewFiles.getSelectionModel().getSelectedItem() != null) {
            String filePath = tableViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath();
            switch (getOperativeSystem()) {
                case "windows":
                    Runtime.getRuntime().exec("cmd /c start \"\" \"" + filePath + "\"");
                    break;
                case "mac os x":
                    Runtime.getRuntime().exec("open \"" + filePath + "\"");
                    break;
                case "linux":
                    ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", filePath);
                    processBuilder.start();
                    break;
                default:
                    DialogGenerator.getDialog(new AlertMessage(
                            "Se desconoce su sistema operativo para abrir el archivo", AlertStatus.ERROR));
                    break;
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Selecciona un archivo para visualizar",
                    AlertStatus.WARNING));
        }
    }

    private String getOperativeSystem() {
        String operativeSystem = System.getProperty("os.name").toLowerCase();
        if (operativeSystem.contains("win")) {
            operativeSystem = "windows";
        }
        return operativeSystem;
    }

    @FXML
    private void addFile() throws IOException {
        if (!getEvidenceDirectory().exists()) {
            getEvidenceDirectory().mkdirs();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Evidencia");
        File evidenceFile = fileChooser.showOpenDialog(new Stage());
        if (evidenceFile != null) {
            copyFile(evidenceFile);
        }
        fillTableViewFiles();
    }

    private void copyFile(File file) throws IOException {
        File fileToSave = new File(getEvidenceDirectory().getAbsolutePath()+"/"+file.getName());
        if (fileToSave.exists()) {
            if (confirmedCopyFile(file.getName())) {
                fileToSave.delete();
                Files.copy(file.toPath(), fileToSave.toPath());
            }
        } else {
            Files.copy(file.toPath(), fileToSave.toPath());
        }
        fillTableViewFiles();
    }

    public boolean confirmedCopyFile(String fileName) {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "Ya existe un archivo con el nombre " + fileName + ", ¿Desea sobreescribirlo?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }



    @FXML
    private void deleteFile() {
        File file = tableViewFiles.getSelectionModel().getSelectedItem();
        if (tableViewFiles.getSelectionModel().getSelectedItem() != null) {
            if (confirmedDeleteEvidence(file.getName())) {
                file.delete();
                fillTableViewFiles();
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Selecciona un archivo para eliminarlo",
                    AlertStatus.WARNING));
        }
    }

    public boolean confirmedDeleteEvidence(String fileName) {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea elimar el archivo " + fileName +"?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    private File getEvidenceDirectory() {
        File directory = null;
        try {
            directory = new File(System.getProperty("user.home")
                    +"/IdeaProjects/SSPGER/evidences/"
                    +getProjectID()+"/"
                    +TransferAdvancement.getAdvancementID()+"/"
                    +getStudentUserID());
        } catch (SQLException evidenceDirectoryException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al buscar la carpeta de evidencias", AlertStatus.ERROR));
            logger.error(evidenceDirectoryException);
        }

        return directory;

    }

    private int getProjectID() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();

        return advancementDAO.getProjectIDByAdvancementID(TransferAdvancement.getAdvancementID());

    }

    private String getStudentID() throws SQLException {
        EvidenceDAO evidenceDAO = new EvidenceDAO();

        return evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId());

    }

    private int getStudentUserID() throws SQLException {
        UserDAO userDAO = new UserDAO();

        return userDAO.getUserIDByStudentID(getStudentID());

    }

    @Override
    public void redirectToAdvancements() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView(
                    "advancementsmanagement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView(
                    "studentadvancement-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToEvidences() throws  IOException, SQLException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("professorevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentevidences-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToProjects() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor") ||
                LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectproposals-view.fxml",1000,600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView("studentviewprojects-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    @Override
    public void redirectToRequest() throws IOException {
        if (LoginController.sessionDetails.getUserType().equals("Profesor")
                || LoginController.sessionDetails.getUserType().equals("RepresentanteCA")) {
            MainStage.changeView("projectrequests-view.fxml", 1000, 600 + MainStage.HEIGHT_OFFSET);
        } else {
            MainStage.changeView(
                    "studentprojectrequestdetails-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
        }
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog(
                "¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.orElse(null) == DialogGenerator.BUTTON_YES);
    }

    @Override
    public void actionLogOut() throws IOException {
        if (confirmedLogOut()) {
            SessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }

}
