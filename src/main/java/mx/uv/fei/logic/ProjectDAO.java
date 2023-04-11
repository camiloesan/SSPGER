package mx.uv.fei.logic;

import mx.uv.fei.dao.IProject;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectDAO implements IProject{
    public int addProject(Project project) throws SQLException {
        int result;
        String sqlQuery = "INSERT INTO Proyectos (clavecuerpoacademico, nombreproyectoinvestigación, lgac, lineainvestigacion, duracionaprox, id_modalidadtr, nombretrabajorecepcional, id_director, alumnosparticipantes, descripcionproyectoinvestigacion, descripciontrabajorecepcional, resultadosesperados, bibliografiarecomendada, estado, etapa, nrc) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1,project.getAcademicBodyId());
        preparedStatement.setString(2,project.getInvestigationProjectName());
        preparedStatement.setInt(3,project.getLGAC_Id());
        preparedStatement.setString(4,project.getInvestigationLine());
        preparedStatement.setString(5,project.getApproximateDuration());
        preparedStatement.setInt(6,project.getModalityId());
        preparedStatement.setString(7,project.getReceptionWorkName());
        preparedStatement.setInt(8,project.getDirectorID());
        preparedStatement.setInt(9,project.getStudentsParticipating());
        preparedStatement.setString(10,project.getInvestigationProjectDescription());
        preparedStatement.setString(11,project.getReceptionWorkDescription());
        preparedStatement.setString(12,project.getExpectedResults());
        preparedStatement.setString(13,project.getRecommendedBibliography());
        preparedStatement.setString(14,project.getState());
        preparedStatement.setString(15,project.getStage());
        preparedStatement.setInt(16,project.getNRC());

        result = preparedStatement.executeUpdate(sqlQuery);
        databaseManager.closeConnection();

        return result;
    }

    public int updateProjectState(int projectId, String state) throws SQLException {
        int result;
        String sqlQuery = "UPDATE Proyectos SET estado = (?) WHERE ID_proyecto = (?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, state);
        preparedStatement.setInt(2,projectId);

        result = preparedStatement.executeUpdate(sqlQuery);
        databaseManager.closeConnection();

        return result;
    }
}
