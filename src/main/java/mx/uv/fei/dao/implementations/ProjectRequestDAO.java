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
/**
 * Provides a set of methods to connect to the Project Request on the database.
 */
public class ProjectRequestDAO implements IProjectRequest {

    /**
     * @param projectRequest new Project Request object
     * @return rows affected, if the Project Request was added or not.
     * @throws SQLException if there was a problem with the database.
     */
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

    /**
     * @param validation new validation of the Project Request.
     * @param projectRequestID reference of Project Request to update.
     * @return rows affected, if the Project Request was validated or not.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int validateProjectRequest(String validation, int projectRequestID) throws SQLException {
        int result;
        String query = "UPDATE SolicitudesProyecto SET estado=(?) WHERE ID_solicitudProyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, validation);
        preparedStatement.setInt(2, projectRequestID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param projectRequestID reference of Project Request to delete.
     * @return rows affected, if the Project Request was validated or not.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int deleteProjectRequest(int projectRequestID) throws SQLException {
        int result;
        String query = "DELETE FROM SolicitudesProyecto WHERE ID_solicitudProyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, projectRequestID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public List<ProjectRequest> getProjectRequestsListByProfessorId(int professorID) throws SQLException {
        String query = "select Soli.ID_solicitudProyecto, Soli.matriculaEstudiante, Soli.estado, Soli.motivos, " +
                "Soli.ID_proyecto from " +
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

        databaseManager.closeConnection();

        return projectRequestList;
    }

    /**
     * @param studentID reference of Project Request to get number of project requests.
     * @return integer with the number of project requests.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int getProjectRequestsByStudentID(String studentID) throws SQLException {
        int result = 0;
        String query = "SELECT COUNT(matriculaEstudiante) AS matriculaEstudiante FROM SolicitudesProyecto " +
                "WHERE matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result = resultSet.getInt("matriculaEstudiante");
        }

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param studentID reference of Project Request to get Project Request ID.
     * @return integer with project request ID.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int getProjectRequestIDByStudentID(String studentID) throws SQLException {
        int result = 0;
        String query = "SELECT ID_solicitudProyecto FROM SolicitudesProyecto WHERE matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result = resultSet.getInt("ID_solicitudProyecto");
        }

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param studentID reference of Project Request to get Project Request information.
     * @return object ProjectRequest with project request information.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public ProjectRequest getProjectRequestInfoByStudentID(String studentID) throws SQLException{
        ProjectRequest projectRequest = new ProjectRequest();
        String query = "SELECT Proyectos.nombreTrabajoRecepcional, SolicitudesProyecto.estado, motivos" +
                " FROM SolicitudesProyecto " +
                "INNER JOIN Proyectos on SolicitudesProyecto.ID_proyecto = Proyectos.ID_proyecto " +
                "WHERE SolicitudesProyecto.matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            projectRequest.setProjectName(resultSet.getString("nombreTrabajoRecepcional"));
            projectRequest.setStatus(resultSet.getString("estado"));
            projectRequest.setDescription(resultSet.getString("motivos"));
        }

        databaseManager.closeConnection();
        return projectRequest;
    }

}
