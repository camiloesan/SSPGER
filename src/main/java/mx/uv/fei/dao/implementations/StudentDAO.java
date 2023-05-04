package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IStudent;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO implements IStudent {
    @Override
    public int insertStudent(Student student) throws SQLException{
        int result;
        String query = "INSERT INTO Estudiantes(matricula, nombre, apellidos, correoInstitucional, nombreUsuario) VALUES(?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, student.getStudentID());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setString(4, student.getAcademicEmail());
        preparedStatement.setString(5, student.getUsername());
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int deleteStudent(String studentID) throws SQLException {
        int result;
        String query = "DELETE FROM Estudiantes WHERE matricula=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public String getNamebyStudentID(String studentID) throws SQLException {
        String query = "SELECT nombre FROM Estudiantes WHERE matricula = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("nombre");
        }

        databaseManager.closeConnection();

        return result;
    }
    
    @Override
    public String getStudentIdByUsername(String username) throws SQLException {
        String sqlQuery = "SELECT matricula FROM Estudiantes WHERE nombreUsuario = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        String studentID = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                studentID = resultSet.getString("matricula");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return studentID;
    }
}
