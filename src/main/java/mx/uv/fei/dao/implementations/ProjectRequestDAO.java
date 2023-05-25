package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IProjectRequest;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.ProjectRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectRequestDAO implements IProjectRequest {
    @Override
    public int createProjectRequest(ProjectRequest projectRequest) throws SQLException {
        int result;
        String query = "INSERT INTO SolicitudesProyecto(ID_proyecto, matriculaEstudiante, motivos) VALUES(?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, projectRequest.getProjectID());
        preparedStatement.setString(2, projectRequest.getStudentId());
        preparedStatement.setString(3, projectRequest.getDescription());
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

    @Override
    public List<ProjectRequest> getProjectRequestsListByProfessorId(int professorID) throws SQLException {
        String query = "select Soli.ID_solicitudProyecto, Soli.matriculaEstudiante, Soli.estado, Soli.motivos, Soli.ID_proyecto from " +
                "SolicitudesProyecto Soli JOIN Proyectos Proy Join Profesores Prof " +
                "on Proy.ID_proyecto = Soli.ID_proyecto and Proy.ID_director = Prof.ID_profesor " +
                "where ID_profesor = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, professorID);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<ProjectRequest> projectRequestList = new ArrayList<>();
        while (resultSet.next()) {
            ProjectRequest projectRequest = new ProjectRequest();
            projectRequest.setProjectPetitionID(resultSet.getInt("ID_solicitudProyecto"));
            projectRequest.setStudentId(resultSet.getString("matriculaEstudiante"));
            projectRequest.setStatus(resultSet.getString("estado"));
            projectRequest.setDescription(resultSet.getString("motivos"));
            projectRequest.setProjectID(resultSet.getInt("ID_proyecto"));
            projectRequestList.add(projectRequest);
        }

        return projectRequestList;
    }



    @Override
    public int getProjectRequestsByStudentID(String studentID, int projectID) throws SQLException {
        int result = 0;
        String query = "SELECT COUNT(matriculaEstudiante) AS matriculaEstudiante FROM SolicitudesProyecto " +
                "INNER JOIN Proyectos ON SolicitudesProyecto.ID_proyecto = Proyectos.ID_proyecto " +
                "WHERE matriculaEstudiante = ? AND SolicitudesProyecto.ID_proyecto = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);
        preparedStatement.setInt(2, projectID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result = resultSet.getInt("matriculaEstudiante");
        };

        databaseManager.closeConnection();
        return result;
    }

    public int getProjecRequestIDByStudentID(String studentID) throws SQLException {
        int result = 0;
        String query = "SELECT ID_solicitudProyecto FROM SolicitudesProyecto WHERE matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result = resultSet.getInt("ID_solicitudProyecto");
        };

        databaseManager.closeConnection();
        return result;
    }

}
