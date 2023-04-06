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
        //preparedStatement.setInt(3, );//las llaves foráneas se obtienen de alguna manera mientras se detecte el clic anterior
        //preparedStatement.setInt(4, );
        //preparedStatement.setInt(5, );
        //preparedStatement.setString(6, );
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    //getStudentMatriculaByName or something

    @Override
    public void addProjectEvidenceGradeById(int id) throws SQLException {

    }

    @Override
    public void getProjectEvidenceByStudentName(String studentName) throws SQLException {

    }
}
