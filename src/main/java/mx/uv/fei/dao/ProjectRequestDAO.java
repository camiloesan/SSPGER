package mx.uv.fei.dao;

import mx.uv.fei.dao.IProjectRequest;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.ProjectRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectRequestDAO implements IProjectRequest {
    @Override
    public int createProjectRequest(ProjectRequest projectPetition) throws SQLException {
        int result;
        String query = "INSERT INTO SolicitudesProyecto(ID_proyecto, matriculaEstudiante, motivos) VALUES(?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, projectPetition.getProjectID());
        preparedStatement.setString(2, projectPetition.getStudentTuition());
        preparedStatement.setString(3, projectPetition.getDescription());
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int validateProjectRequest(String validation, int projectPetitionID) throws SQLException {
        int result;
        String query = "UPDATE SolicitudesProyecto SET estado=(?) WHERE ID_solicitudProyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, validation);
        preparedStatement.setInt(2, projectPetitionID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int deleteProjectRequest(int projectPetitionID) throws SQLException {
        int result;
        String query = "DELETE FROM SolicitudesProyecto WHERE ID_solicitudProyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, projectPetitionID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

}
