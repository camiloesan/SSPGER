package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;

public class EvidenceFilesController implements IStudentNavigationBar {
    @FXML
    private Label labelUsername;
    @FXML
    private TableView<File> tableViewFiles;
    @FXML
    private Button buttonAddFile;
    @FXML
    private Button buttonDeleteFile;
    private static final Logger logger = Logger.getLogger(EvidenceFilesController.class);

    public void initialize() {

        if (getEvidenceDirectory().exists()) {
            fillTableViewFiles();
        }

        labelUsername.setText(SessionDetails.getInstance().getUsername());

        if (SessionDetails.getInstance().getUserType().equals(LoginController.USER_STUDENT)) {
            buttonAddFile.setVisible(true);
            buttonDeleteFile.setVisible(true);
        }
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

    private void alertErrorOpenFile() {
        DialogGenerator.getDialog(new AlertMessage("Error al intentar abrir el archivo", AlertStatus.ERROR));
    }

    @FXML
    private void visualizeFile() {
        if (tableViewFiles.getSelectionModel().getSelectedItem() != null) {
            String operativeSystem = System.getProperty("os.name").toLowerCase();
            String filePath = tableViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath();
            switch (operativeSystem) {
                case "windows":
                    try {
                        Runtime.getRuntime().exec("cmd /c start \"\" \"" +
                                filePath + "\"");
                    } catch (IOException ioException) {
                        alertErrorOpenFile();
                        logger.error(ioException);
                    }
                    break;
                case "mac os x":
                    try {
                        Runtime.getRuntime().exec("open \"" + filePath + "\"");
                    } catch (IOException ioException) {
                        alertErrorOpenFile();
                        logger.error(ioException);
                    }
                    break;
                case "linux":
                    ProcessBuilder processBuilder = new ProcessBuilder("xdg-open",
                            filePath);
                    try {
                        processBuilder.start();
                    } catch (IOException ioException) {
                        alertErrorOpenFile();
                        logger.error(ioException);
                    }
                    break;
                default:
                    DialogGenerator.getDialog(new AlertMessage(
                            "Se desconoce su sistema operativo", AlertStatus.ERROR));
                    break;
            }
        } else {
            DialogGenerator.getDialog(new AlertMessage("Selecciona un archivo para visualizar",
                    AlertStatus.WARNING));
        }
    }

    @FXML
    private void addFile() throws IOException {
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
        Files.copy(file.toPath(), fileToSave.toPath());
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
        EvidenceDAO evidenceDAO = new EvidenceDAO();
        String projectName = null;
        String advancementName = null;
        String studentID = null;
        try {
            projectName = evidenceDAO.getProjectNameByEvidenceID(TransferEvidence.getEvidenceId());
            advancementName = evidenceDAO.getAdvancementNameByStudentID(
                    evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId()),
                    evidenceDAO.getAdvancementIDByEvidenceID(TransferEvidence.getEvidenceId()));
            studentID = evidenceDAO.getStudentIDByEvidenceID(TransferEvidence.getEvidenceId());
        } catch(SQLException evidenceDAOException) {
            DialogGenerator.getDialog(new AlertMessage(
                    "Error al recuperar información para la carpeta de evidencias", AlertStatus.ERROR));
        }
        return new File(System.getProperty("user.home")
                +"/IdeaProjects/SSPGER/evidences/"
                +projectName+"/"
                +advancementName+"/"
                +studentID);
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
                    "studentprojectrequest-view.fxml",1000, 600 + MainStage.HEIGHT_OFFSET);
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
