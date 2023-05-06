package mx.uv.fei.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mx.uv.fei.dao.implementations.AccessAccountDAO;
import mx.uv.fei.logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class AccessAccountManagementController {
    @FXML
    private ComboBox<String> comboBoxUserType;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private Tab tabUsers;
    @FXML
    private GridPane gridPaneProfessor;
    @FXML
    private GridPane gridPaneStudent;
    @FXML
    private ComboBox<String> comboBoxDegree;
    @FXML
    private TextField textFieldStudentId;
    @FXML
    private TextField textFieldStudentName;
    @FXML
    private TextField textFieldStudentLastName;
    @FXML
    private TextField textFieldStudentEmail;
    @FXML
    private TextField textFieldProfessorName;
    @FXML
    private TextField textFieldProfessorLastName;
    @FXML
    private TextField textFieldProfessorEmail;
    @FXML
    private TableView<AccessAccount> tableViewAccessAccounts;
    private final static ObservableList<String> observableListComboItemsUserType =
            FXCollections.observableArrayList("Administrador", "Estudiante", "Profesor", "RepresentanteCA");
    private final static ObservableList<String> observableListComboItemsDegree =
            FXCollections.observableArrayList("Dr." ,"Dra.", "MCC.");
    private static String username;

    @FXML
    private void initialize() {
        TableColumn<AccessAccount, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idColumn.setMinWidth(30);
        idColumn.setMaxWidth(30);
        TableColumn<AccessAccount, String> usernameColumn = new TableColumn<>("Usuario");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<AccessAccount, String> userTypeColumn = new TableColumn<>("Tipo de usuario");
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        tableViewAccessAccounts.getColumns().addAll(idColumn, usernameColumn, userTypeColumn);
        fillTableViewAccessAccounts();
        comboBoxDegree.setItems(observableListComboItemsDegree);
        comboBoxUserType.setItems(observableListComboItemsUserType);
    }

    private void fillTableViewAccessAccounts() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        try {
            tableViewAccessAccounts.getItems().addAll(accessAccountDAO.getAccessAccountsList());
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo recuparar la informaicón de la base de datos, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void buttonSaveAction() {
        if (areAccessAccountFieldsValid()) {
            switch (comboBoxUserType.getValue()) {
                case "Profesor", "RepresentanteCA" -> {
                    if (areProfessorFieldsValid()) {
                        addProfessorUser();
                    }
                }
                case "Estudiante" -> {
                    if (areStudentFieldsValid()) {
                        addStudentUser();
                    }
                }
                case "Administrador" -> addAdminUser();
                default -> DialogGenerator.getDialog(new AlertMessage("Debes seleccionar un tipo de usuario", AlertStatus.WARNING));
            }
        }
        tableViewAccessAccounts.getItems().clear();
        fillTableViewAccessAccounts();
    }

    private void addProfessorUser() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        Professor professor = new Professor();
        professor.setProfessorName(textFieldProfessorName.getText());
        professor.setProfessorLastName(textFieldProfessorLastName.getText());
        professor.setProfessorDegree(comboBoxDegree.getValue());
        professor.setProfessorEmail(textFieldProfessorEmail.getText());
        try {
            accessAccountDAO.transactionAddProfessorUser(accessAccount, professor);
            DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    private void addStudentUser() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        Student student = new Student();
        student.setStudentID(textFieldStudentId.getText());
        student.setName(textFieldStudentName.getText());
        student.setLastName(textFieldStudentLastName.getText());
        student.setAcademicEmail(textFieldStudentEmail.getText());
        try {
            accessAccountDAO.transactionAddStudentUser(accessAccount, student);
            DialogGenerator.getDialog(new AlertMessage("Se agregó al usuario satisfactoriamente", AlertStatus.SUCCESS));
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo añadir al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    private void addAdminUser() {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername(textFieldUsername.getText());
        accessAccount.setUserPassword(passwordFieldPassword.getText());
        accessAccount.setUserType(comboBoxUserType.getValue());
        try {
            accessAccountDAO.addAdminAccessAccount(accessAccount);
        } catch (SQLException sqlException) {
            DialogGenerator.getDialog(new AlertMessage("No se pudo agregar al usuario, inténtelo de nuevo más tarde", AlertStatus.ERROR));
        }
    }

    @FXML
    private void buttonModifyAction() throws IOException {
        if (tableViewAccessAccounts.getSelectionModel().getSelectedItem() != null) {
            setUsername(tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername());
            Parent modifyUser = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("panemodifyuser-view.fxml")));
            tabUsers.setContent(modifyUser);
        } else {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar a un usuario para modificarlo", AlertStatus.WARNING));
        }
    }

    @FXML
    private void buttonDeleteAction() throws SQLException {
        if (tableViewAccessAccounts.getSelectionModel().getSelectedItem() == null) {
            DialogGenerator.getDialog(new AlertMessage("Debes seleccionar al usuario que quieres eliminar", AlertStatus.WARNING));
        } else {
            String username = tableViewAccessAccounts.getSelectionModel().getSelectedItem().getUsername();
            if (isUserAdmin(username)) {
                DialogGenerator.getDialog(new AlertMessage("No se pueden eliminar los usuarios administrador", AlertStatus.WARNING));
            } else {
                deleteUser(username);
                tableViewAccessAccounts.getItems().clear();
                fillTableViewAccessAccounts();
            }
        }
    }

    @FXML
    private void handleAddUserTypeSelection() {
        switch (comboBoxUserType.getValue()) {
            case "Profesor", "RepresentanteCA" -> {
                gridPaneStudent.setVisible(false);
                gridPaneProfessor.setVisible(true);
            }
            case "Estudiante" -> {
                gridPaneStudent.setVisible(true);
                gridPaneProfessor.setVisible(false);
            }
            default -> {
                gridPaneProfessor.setVisible(false);
                gridPaneStudent.setVisible(false);
            }
        }
    }

    @FXML
    private void actionLogOut() throws IOException {
        logOut();
    }

    private static final int MAX_LENGTH_USERNAME = 28;
    private static final int MAX_LENGTH_PASSWORD = 64;
    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_LASTNAME = 80;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_STUDENT_ID = 10;

    private boolean areAccessAccountFieldsValid() {
        if (textFieldUsername.getText().isBlank()
                || passwordFieldPassword.getText().isBlank()
                || comboBoxUserType.getValue() == null) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldUsername.getText().length() > MAX_LENGTH_USERNAME
                || passwordFieldPassword.getText().length() > MAX_LENGTH_PASSWORD){
            DialogGenerator.getDialog(new AlertMessage("Has sobrepasado el límite de caracteres, inténtalo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean areProfessorFieldsValid() {
        if (textFieldProfessorName.getText().isBlank()
                || textFieldProfessorLastName.getText().isBlank()
                || comboBoxDegree.getValue() == null
                || textFieldProfessorEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if(textFieldProfessorName.getText().length() > MAX_LENGTH_NAME
                || textFieldProfessorLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldProfessorEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean areStudentFieldsValid() {
        if (textFieldStudentId.getText().isBlank()
                || textFieldStudentName.getText().isBlank()
                || textFieldStudentLastName.getText().isBlank()
                || textFieldStudentEmail.getText().isBlank()) {
            DialogGenerator.getDialog(new AlertMessage("Todos los campos deben estar llenos", AlertStatus.WARNING));
            return false;
        } else if (textFieldStudentId.getText().length() > MAX_LENGTH_STUDENT_ID
                || textFieldStudentName.getText().length() > MAX_LENGTH_NAME
                || textFieldStudentLastName.getText().length() > MAX_LENGTH_LASTNAME
                || textFieldStudentEmail.getText().length() > MAX_LENGTH_EMAIL) {
            DialogGenerator.getDialog(new AlertMessage("Algunos campos son demasiado largos, inténtelo de nuevo", AlertStatus.WARNING));
            return false;
        } else {
            return true;
        }
    }

    private boolean isUserAdmin(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        return accessAccountDAO.getAccessAccountTypeByUsername(username).equals("Administrador");
    }

    public boolean confirmedDeleteUser(String displayUsername) {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea eliminar al usuario " + displayUsername + "?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void deleteUser(String username) throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        if(confirmedDeleteUser(username)) {
            accessAccountDAO.deleteUserByUsername(username);
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AccessAccountManagementController.username = username;
    }

    public boolean confirmedLogOut() {
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Está seguro que desea salir, se cerrará su sesión?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }

    private void logOut() throws IOException {
        if (confirmedLogOut()) {
            LoginController.sessionDetails.cleanSessionDetails();
            MainStage.changeView("login-view.fxml", 600, 400 + MainStage.HEIGHT_OFFSET);
        }
    }
}
