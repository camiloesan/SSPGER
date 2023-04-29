package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IProfessor;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO implements IProfessor {
    @Override
    public int addProfessor(Professor professor) throws SQLException{
        int result;
        String sqlQuery = "INSERT INTO Profesores (grado, nombre, apellidos, correoInstitucional, ID_usuario) VALUES (?,?,?,?,?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1,professor.getProfessorDegree());
        preparedStatement.setString(2, professor.getProfessorName());
        preparedStatement.setString(3,professor.getProfessorLastName());
        preparedStatement.setString(4,professor.getProfessorEmail());
        preparedStatement.setInt(5,professor.getUserId());

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }
    @Override
    public int updateProfessorByName(Professor updatedProfessor, String professorNameToUpdate) throws SQLException {
        int result;
        String sqlQuery = "UPDATE Profesores SET grado = (?), nombre = (?), apellidos = (?), correoInstitucional = (?) WHERE nombre = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1,updatedProfessor.getProfessorDegree());
        preparedStatement.setString(2,updatedProfessor.getProfessorName());
        preparedStatement.setString(3,updatedProfessor.getProfessorLastName());
        preparedStatement.setString(4,updatedProfessor.getProfessorEmail());
        
        preparedStatement.setString(5, professorNameToUpdate);
        
        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        
        return result;
    }
    
    @Override
    public List<Professor> getAllProfessors() throws SQLException {
        String sqlQuery = "SELECT grado, nombre, apellidos, correoInstitucional FROM Profesores";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Professor> professors = new ArrayList<>();
        while (resultSet.next()) {
            Professor professor = new Professor();
            professor.setProfessorName(resultSet.getString("nombre"));
            professor.setProfessorLastName(resultSet.getString("apellidos"));
            professor.setProfessorEmail(resultSet.getString("correoInstitucional"));
            
            professors.add(professor);
        }
        databaseManager.closeConnection();
        
        return professors;
    }
    
    @Override
    public int deleteProfessorByName(String professorNameToDelete) throws SQLException {
        int result;
        String sqlQuery = "DELETE FROM Profesores WHERE nombre = (?)";
        
        DatabaseManager databaseManager = new  DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1,professorNameToDelete);
        
        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        
        return result;
    }
    
    @Override
    public List<String> getProfessorsNames() throws SQLException {
        String sqlQuery = "SELECT CONCAT(grado, ' ',nombre, ' ', apellidos) AS nombreCompleto FROM Profesores;";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> professorsNames = new ArrayList<>();
        while (resultSet.next()) {
            Professor professor = new Professor();
            professor.setProfessorFullName(resultSet.getString("nombreCompleto"));
            
            professorsNames.add(professor.getProfessorFullName());
        }
        databaseManager.closeConnection();
        
        return professorsNames;
    }
}
