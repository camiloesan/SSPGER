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
}
