package mx.uv.fei.gui;

import javafx.fxml.FXML;
import mx.uv.fei.dao.AccessAccountDAO;
import mx.uv.fei.logic.AccessAccount;

import java.sql.SQLException;

public class CRUDAccessAccountController {
    @FXML
    private void updateListView() throws SQLException {
        listViewAccessAccounts.getItems().clear(); //important variable name
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        for(AccessAccount eventObject : accessAccountDAO.getListAccessAccounts()) {
            listViewAccessAccounts.getItems().add(eventObject.getUsername());
        }
    }
    @FXML
    private void initialize() {
        updateListView();
    }
}
