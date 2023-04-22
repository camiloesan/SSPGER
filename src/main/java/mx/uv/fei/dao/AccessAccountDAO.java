package mx.uv.fei.dao;

import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.AccessAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccessAccountDAO implements IAccessAccount {
    @Override
    public void addAccessAccount(AccessAccount accessAccount) throws SQLException {
        String query = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUsername());
        preparedStatement.setString(2, accessAccount.getUserPassword());
        preparedStatement.setString(3, accessAccount.getUserType());
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void modifyAccessAccountByUsername(String username, AccessAccount accessAccount) throws SQLException {
        String query = "update CuentasAcceso set nombreUsuario=(?), contrasena=(SHA2(?, 256)) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUsername());
        preparedStatement.setString(2, accessAccount.getUserPassword());
        preparedStatement.setString(3, username);
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void deleteAccessAccountByUsername(String username) throws SQLException {
        String query = "delete from CuentasAcceso where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public boolean areCredentialsValid(String username, String password) throws SQLException {
        boolean isValid;
        String query = "select 1 from CuentasAcceso where nombreUsuario=(?) and contrasena=(SHA2(?, 256))";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        isValid = resultSet.next();
        databaseManager.closeConnection();

        return isValid;
    }

    @Override
    public String getAccessAccountTypeByUsername(String username) throws SQLException {
        String query = "select tipoUsuario from CuentasAcceso where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        String type = "null";
        while (resultSet.next()) {
            type = resultSet.getString("tipoUsuario");
        }

        return type;
    }

    @Override
    public List<AccessAccount> getListAccessAccounts() throws SQLException {
        String query = "select nombreUsuario, tipoUsuario from CuentasAcceso";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<AccessAccount> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUsername(resultSet.getString("nombreUsuario"));
            accessAccount.setUserType(resultSet.getString("tipoUsuario"));
            accessAccountList.add(accessAccount);
        }

        return accessAccountList;
    }

    @Override
    public List<AccessAccount> getUsernamesByUsertype(String userType) throws SQLException {
        String query = "select nombreUsuario from CuentasAcceso where tipoUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userType);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<AccessAccount> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUsername(resultSet.getString("nombreUsuario"));
            accessAccount.setUserType(resultSet.getString("tipoUsuario"));
            accessAccountList.add(accessAccount);
        }
        return accessAccountList;
    }

    @Override
    public int modifyPasswordByUsername(AccessAccount accessAccount) throws SQLException {
        String query = "update CuentasAcceso set contrasena=(SHA2(?, 256)), tipoUsuario=(?) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUserPassword());
        preparedStatement.setString(2, accessAccount.getUserType());
        preparedStatement.setString(3, accessAccount.getUsername());
        int result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }
}
