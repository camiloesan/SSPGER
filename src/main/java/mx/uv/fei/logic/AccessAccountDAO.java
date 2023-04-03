package mx.uv.fei.logic;

import mx.uv.fei.dao.IAccessAccount;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccessAccountDAO implements IAccessAccount {
    @Override
    public void addAccessAccount(AccessAccount accessAccount) throws SQLException {
        String query = "insert into CuentasAcceso(ID_usuario, nombreUsuario, contrasena) values (?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accessAccount.getUserId());
        preparedStatement.setString(2, accessAccount.getUsername());
        preparedStatement.setString(3, accessAccount.getUserPassword());
    }

    @Override
    public void modifyAccessAccount(AccessAccount accessAccount) throws SQLException {

    }

    @Override
    public void deleteAccessAccountByName(String name) throws SQLException {

    }
}
