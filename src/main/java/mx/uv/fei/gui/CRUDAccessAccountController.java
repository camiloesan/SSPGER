package mx.uv.fei.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUDAccessAccountController {
    @FXML
    private TableView<ObservableList> tableViewAccessAccounts;
    @FXML
    private Button buttonAddNewUser;
    @FXML
    private Button buttonModifyUser;
    @FXML
    private Button buttonDeleteUser;

    @FXML
    private void adduser() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        //accessAccountDAO.addAccessAccount(accessAccount);
    }
    @FXML
    private void modifySelectedUser() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();
        tableViewAccessAccounts.getSelectionModel().getSelectedIndex();

        accessAccountDAO.modifyAccessAccountByUsername("", accessAccount);
    }
    @FXML
    private void deleteUser() {

    }
    @FXML
    private void updateListView() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        ObservableList data = FXCollections.observableArrayList();
        ResultSet resultSet = accessAccountDAO.getResultSetAccessAccounts();

        for(int i = 0 ; i < resultSet.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
            tableViewAccessAccounts.getColumns().addAll(col);
        }

        while(resultSet.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i = 1 ; i <= resultSet.getMetaData().getColumnCount(); i++){
                row.add(resultSet.getString(i));
            }
            data.add(row);
        }

        tableViewAccessAccounts.setItems(data);
    }
    @FXML
    private void initialize() throws SQLException {
        updateListView();
    }
}
