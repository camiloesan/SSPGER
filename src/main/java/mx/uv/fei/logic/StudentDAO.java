package mx.uv.fei.logic;

import mx.uv.fei.dao.IStudent;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements IStudent {
    @Override
    public List<Student> getStudents() throws SQLException {
        List<Student> listStudents = new ArrayList<>();
        String query = "SELECT * FROM Estudiantes";
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        do {

            Student objectStudent = new Student();

            objectStudent.setTuition(results.getString("matricula"));
            objectStudent.setName(results.getString("nombre"));
            objectStudent.setLastName(results.getString("apellidoPaterno"));
            objectStudent.setMothersLastName(results.getString("apellidoMaterno"));
            objectStudent.setAcademicEmail(results.getString("correoInstitucional"));
            objectStudent.setNRC(results.getInt("NRC"));
            objectStudent.setUserID(results.getInt("ID_usuario"));

            listStudents.add(objectStudent);
        } while (results.next());
        dataBaseManager.closeConnection();
        return listStudents;
    }

    @Override
    public void insertStudent(Student student) throws SQLException{
        String query = "INSERT INTO Estudiantes(matricula, nombre, apellidoPaterno, apellidoMaterno, correoInstitucional, NRC, ID_usuario) VALUES(?,?,?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, student.getTuition());
        statement.setString(2, student.getName());
        statement.setString(3, student.getLastName());
        statement.setString(4, student.getMothersLastName());
        statement.setString(5, student.getAcademicEmail());
        statement.setInt(6, student.getNRC());
        statement.setInt(7, student.getUserID());

        databaseManager.closeConnection();
    }
}
