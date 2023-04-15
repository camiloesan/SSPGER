package mx.uv.fei.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.sql.SQLException;

public class CRUDAccessAccountController {
    @FXML
    private TableView<AccessAccount> tableViewAccessAccounts;
    @FXML
    private TableColumn<AccessAccount, String> tableColumnUsername;
    @FXML
    private TableColumn<AccessAccount, String> tableColumnUserType;

    @FXML
    private void updateListView() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();

        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("usernameProperty"));
        tableColumnUserType.setCellValueFactory(new PropertyValueFactory<>("userTypeProperty"));
        tableViewAccessAccounts.getItems().setAll(accessAccountDAO.getListAccessAccounts());

        /*
        for(AccessAccount accessAccountObject : accessAccountDAO.getListAccessAccounts()) {
            tableViewAccessAccounts.getItems().add(accessAccountObject);

            System.out.println(accessAccountObject.getUsername());
            System.out.println(accessAccountObject.getUserType());
        }*/
    }
    @FXML
    private void initialize() throws SQLException {
        updateListView();
    }
}
