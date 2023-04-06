package mx.uv.fei.logic;

import mx.uv.fei.dao.IProjectEvidence;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectEvidenceDAO implements IProjectEvidence {
    @Override
    public void addProjectEvidence(ProjectEvidence projectEvidence) throws SQLException {
        String query = "insert into Evidencias(titulo, descripcion, ID_profesor, ID_avance, ID_proyecto, matriculaEstudiante) values (?,?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, projectEvidence.getProjectEvidenceTitle());
        preparedStatement.setString(2, projectEvidence.getProjectEvidenceDescription());
        preparedStatement.setInt(3, projectEvidence.getProfessorId());
        preparedStatement.setInt(4, projectEvidence.getProgressId());
        preparedStatement.setInt(5, projectEvidence.getProjectId());
        preparedStatement.setString(6, projectEvidence.getStudentMatricula());
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void updateProjectEvidenceGradeByMatricula(String matricula, int grade) throws SQLException {
        String query = "update Evidencias set calificacion=(?) where matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, grade);
        preparedStatement.setString(2, matricula);
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void getProjectEvidenceByStudentName(String studentName) throws SQLException {

    }
}
