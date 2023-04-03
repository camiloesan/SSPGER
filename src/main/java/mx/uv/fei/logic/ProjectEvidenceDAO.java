package mx.uv.fei.logic;

import mx.uv.fei.dao.IProjectEvidence;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectEvidenceDAO implements IProjectEvidence {

    @Override
    public List<ProjectEvidence> getProjectEvidence() throws SQLException {
        List<ProjectEvidence> listEvidence = new ArrayList<>();
        String query = "SELECT * FROM Evidencias";
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        do {

            ProjectEvidence objectEvidence = new ProjectEvidence();

            objectEvidence.setProjectEvidenceId(results.getInt("ID_evidencia"));
            objectEvidence.setProjectEvidenceTitle(results.getString("titulo"));
            objectEvidence.setProjectEvidenceStatus(results.getString("estado"));
            objectEvidence.setProjectEvidenceGrade(results.getInt("calificacion"));
            objectEvidence.setProjectEvidenceDescription(results.getString("descripciom"));
            objectEvidence.setProfessorId(results.getInt("ID_profesor"));
            objectEvidence.setProfessorId(results.getInt("ID_avance"));
            objectEvidence.setProjectId(results.getInt("ID_proyecto"));
            objectEvidence.setStudentMatricula(results.getString("matriculaEstudiante"));

            listEvidence.add(objectEvidence);
        } while (results.next());
        dataBaseManager.closeConnection();
        return listEvidence;
    }
}
