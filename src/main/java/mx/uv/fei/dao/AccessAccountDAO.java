package mx.uv.fei.dao;

import javafx.scene.chart.PieChart;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccessAccountDAO implements IAccessAccount {
    @Override
    public void addAdminAccessAccount(AccessAccount accessAccount) throws SQLException {
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
    public void modifyStudentUserTransaction(String username, AccessAccount accessAccount, Student student) throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), tipoUsuario=(?) where nombreUsuario=(?)";
        String secondQuery = "update Estudiantes set matricula=(?), nombre=(?), apellidos=(?), correoInstitucional=(?) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUserPassword());
            firstPreparedStatement.setString(2, accessAccount.getUserType());
            firstPreparedStatement.setString(3, username);
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, student.getAcademicEmail());
            secondPreparedStatement.setString(5, username);
            firstPreparedStatement.executeUpdate();
            secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
    }

    @Override
    public void modifyProfessorUserTransaction(String username, AccessAccount accessAccount, Professor professor) throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), tipoUsuario=(?) where nombreUsuario=(?)";
        String secondQuery = "update Profesores set nombre=(?), apellidos=(?), grado=(?), correoInstitucional=(?) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUserPassword());
            firstPreparedStatement.setString(2, accessAccount.getUserType());
            firstPreparedStatement.setString(3, username);
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, professor.getProfessorName());
            secondPreparedStatement.setString(2, professor.getProfessorLastName());
            secondPreparedStatement.setString(3, professor.getProfessorDegree());
            secondPreparedStatement.setString(4, professor.getProfessorEmail());
            secondPreparedStatement.setString(5, username);
            firstPreparedStatement.executeUpdate();
            secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
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
    public List<String> getListAccessAccounts() throws SQLException {
        String query = "select nombreUsuario, tipoUsuario from CuentasAcceso";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<String> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            accessAccountList.add(resultSet.getString("nombreUsuario"));
        }

        return accessAccountList;
    }

    @Override
    public List<String> getUsernamesByUsertype(String userType) throws SQLException {
        String query = "select nombreUsuario from CuentasAcceso where tipoUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userType);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<String> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            accessAccountList.add(resultSet.getString("nombreUsuario"));
        }

        return accessAccountList;
    }

    @Override
    public int addStudentUserTransaction(AccessAccount accessAccount, Student student) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        String secondQuery = "insert into Estudiantes(matricula, nombre, apellidos, correoInstitucional, nombreUsuario) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, accessAccount.getUserType());
            System.out.println("it does");
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, student.getAcademicEmail());
            secondPreparedStatement.setString(5, accessAccount.getUsername());
            firstPreparedStatement.executeUpdate();
            secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
        return 0;
    }

    @Override
    public int addProfessorUserTransaction(AccessAccount accessAccount, Professor professor) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        String secondQuery = "insert into Profesores(nombre, apellidos, grado, correoInstitucional, nombreUsuario) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, accessAccount.getUserType());
            System.out.println("it does");
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, professor.getProfessorName());
            secondPreparedStatement.setString(2, professor.getProfessorLastName());
            secondPreparedStatement.setString(3, professor.getProfessorDegree());
            secondPreparedStatement.setString(4, professor.getProfessorEmail());
            secondPreparedStatement.setString(5, accessAccount.getUsername());
            firstPreparedStatement.executeUpdate();
            secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
        return 0;
    }
}
