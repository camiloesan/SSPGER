package mx.uv.fei.dao;

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
        String sqlQuery = "INSERT INTO Profesores (nombre, apellidos, correoInstitucional, ID_usuario) VALUES (?,?,?,?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, professor.getProfessorName());
        preparedStatement.setString(2,professor.getProfessorLastName());
        preparedStatement.setString(3,professor.getProfessorEmail());
        preparedStatement.setInt(4,professor.getUserId());

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }
    @Override
    public int updateProfessorByName(Professor updatedProfessor, String professorNameToUpdate) throws SQLException {
        int result;
        String sqlQuery = "UPDATE Profesores SET nombre = (?), apellidos = (?), correoInstitucional = (?) WHERE nombre = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1,updatedProfessor.getProfessorName());
        preparedStatement.setString(2,updatedProfessor.getProfessorLastName());
        preparedStatement.setString(3,updatedProfessor.getProfessorEmail());
        
        preparedStatement.setString(4, professorNameToUpdate);
        
        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        
        return result;
    }
    
    @Override
    public List<Professor> getAllProfessors() throws SQLException {
        String sqlQuery = "SELECT nombre, apellidos, correoInstitucional FROM Profesores";
        
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
}
