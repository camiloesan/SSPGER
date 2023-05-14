package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IUser;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.gui.LoginController;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO implements IUser {
    private static final Logger logger = Logger.getLogger(LoginController.class);

    /**
     * Provides DAO functions to get all kinds of information related with the users (administrador,
     * profesor, estudiante, representante del cuerpo académico), also provides an authentication method.
     */
    @Override
    public int addAdminUser(AccessAccount accessAccount) throws SQLException {
        String query = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, accessAccount.getUsername());
        preparedStatement.setString(2, accessAccount.getUserPassword());
        preparedStatement.setString(3, LoginController.USER_ADMIN);

        int result = 0;
        try {
            result = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.error(sqlException.getStackTrace());
        }

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public boolean addStudentUserTransaction(AccessAccount accessAccount, Student student) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        String secondQuery = "insert into Estudiantes(matricula, nombre, apellidos, correoInstitucional, nombreUsuario) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        int resultFirstQuery = 0;
        int resultSecondQuery = 0;

        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, LoginController.USER_STUDENT);
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, student.getAcademicEmail());
            secondPreparedStatement.setString(5, accessAccount.getUsername());
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException sqlException1) {
                logger.error(sqlException1.getStackTrace());
            }
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    @Override
    public boolean addProfessorUserTransaction(AccessAccount accessAccount, Professor professor) throws SQLException {
        String firstQuery = "insert into CuentasAcceso(nombreUsuario, contrasena, tipoUsuario) values (?,SHA2(?, 256),?)";
        String secondQuery = "insert into Profesores(nombre, apellidos, grado, correoInstitucional, nombreUsuario) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUsername());
            firstPreparedStatement.setString(2, accessAccount.getUserPassword());
            firstPreparedStatement.setString(3, accessAccount.getUserType());
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, professor.getProfessorName());
            secondPreparedStatement.setString(2, professor.getProfessorLastName());
            secondPreparedStatement.setString(3, professor.getProfessorDegree());
            secondPreparedStatement.setString(4, professor.getProfessorEmail());
            secondPreparedStatement.setString(5, accessAccount.getUsername());
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    @Override
    public boolean modifyStudentUserTransaction(String username, AccessAccount accessAccount, Student student) throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), tipoUsuario=(?) where nombreUsuario=(?) and tipoUsuario!=(?)";
        String secondQuery = "update Estudiantes set matricula=(?), nombre=(?), apellidos=(?), correoInstitucional=(?) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement firstPreparedStatement = connection.prepareStatement(firstQuery);
            firstPreparedStatement.setString(1, accessAccount.getUserPassword());
            firstPreparedStatement.setString(2, accessAccount.getUserType());
            firstPreparedStatement.setString(3, username);
            firstPreparedStatement.setString(4, LoginController.USER_ADMIN);
            PreparedStatement secondPreparedStatement = connection.prepareStatement(secondQuery);
            secondPreparedStatement.setString(1, student.getStudentID());
            secondPreparedStatement.setString(2, student.getName());
            secondPreparedStatement.setString(3, student.getLastName());
            secondPreparedStatement.setString(4, student.getAcademicEmail());
            secondPreparedStatement.setString(5, username);
            resultFirstQuery = firstPreparedStatement.executeUpdate();
            resultSecondQuery = secondPreparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            connection.rollback();
        } finally {
            databaseManager.closeConnection();
        }
        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    @Override
    public boolean modifyProfessorUserTransaction(String username, AccessAccount accessAccount, Professor professor) throws SQLException {
        String firstQuery = "update CuentasAcceso set contrasena=(SHA2(?, 256)), tipoUsuario=(?) where nombreUsuario=(?)";
        String secondQuery = "update Profesores set nombre=(?), apellidos=(?), grado=(?), correoInstitucional=(?) where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int resultFirstQuery = 0;
        int resultSecondQuery = 0;
        if (Objects.equals(accessAccount.getUserType(), LoginController.USER_ADMIN)) {
            return false;
        } else {
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
                resultFirstQuery = firstPreparedStatement.executeUpdate();
                resultSecondQuery = secondPreparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException sqlException) {
                connection.rollback();
            } finally {
                databaseManager.closeConnection();
            }
        }

        return resultFirstQuery > 0 && resultSecondQuery > 0;
    }

    @Override
    public int deleteUserByUsername(String username) throws SQLException {
        String query = "delete from CuentasAcceso where nombreUsuario=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        int result = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();

        return result;
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
    public List<AccessAccount> getAccessAccountsList() throws SQLException {
        String query = "select ID_usuario, nombreUsuario, tipoUsuario from CuentasAcceso";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<AccessAccount> accessAccountList = new ArrayList<>();
        while(resultSet.next()) {
            AccessAccount accessAccount = new AccessAccount();
            accessAccount.setUserId(resultSet.getInt("ID_usuario"));
            accessAccount.setUsername(resultSet.getString("nombreUsuario"));
            accessAccount.setUserType(resultSet.getString("tipoUsuario"));
            accessAccountList.add(accessAccount);
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
}
