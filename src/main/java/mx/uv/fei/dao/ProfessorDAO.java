package mx.uv.fei.dao;

import mx.uv.fei.dao.IProfessor;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfessorDAO implements IProfessor {
    public int addProfessor(Professor professor) throws SQLException{
        int result;
        String sqlQuery = "INSERT INTO Profesores (nombre, apellidoPaterno, apellidoMaterno, correoInstitucional, ID_usuario) VALUES (?,?,?,?,?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, professor.getProfessorName());
        preparedStatement.setString(2,professor.getProfessorFirsLastName());
        preparedStatement.setString(2,professor.getProfessorSecondLastName());
        preparedStatement.setString(3,professor.getProfessorEmail());
        preparedStatement.setInt(4,professor.getUserId());

        result = preparedStatement.executeUpdate(sqlQuery);
        databaseManager.closeConnection();

        return result;
    }
}
