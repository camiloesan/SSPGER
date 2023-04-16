package mx.uv.fei.dao;

import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDAO implements IStudent {
    @Override
    public int insertStudent(Student student) throws SQLException{
        int result;
        String query = "INSERT INTO Estudiantes(matricula, nombre, apellidos, correoInstitucional, NRC, ID_usuario) VALUES(?,?,?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, student.getStudentID());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setString(4, student.getAcademicEmail());
        preparedStatement.setInt(5, student.getNRC());
        preparedStatement.setInt(6, student.getUserID());
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int deleteStudent(String studentTuition) throws SQLException {
        int result;
        String query = "DELETE FROM Estudiantes WHERE matricula=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentTuition);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

}
