package mx.uv.fei.dao;

import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Evidence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvidenceDAO implements IEvidence {
    @Override
    public void addProjectEvidence(Evidence evidence) throws SQLException {
        String query = "insert into Evidencias(titulo, descripcion, ID_profesor, ID_avance, ID_proyecto, matriculaEstudiante) values (?,?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidence.getProjectEvidenceTitle());
        preparedStatement.setString(2, evidence.getProjectEvidenceDescription());
        preparedStatement.setInt(3, evidence.getProfessorId());
        preparedStatement.setInt(4, evidence.getProgressId());
        preparedStatement.setInt(5, evidence.getProjectId());
        preparedStatement.setString(6, evidence.getStudentId());
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void updateProjectEvidenceGradeById(String id, int grade) throws SQLException {
        String query = "update Evidencias set calificacion=(?) where matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, grade);
        preparedStatement.setString(2, id);
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void getProjectEvidenceByStudentId(String id) throws SQLException {
        String query = "select * from Evidencias where matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();
    }
}
