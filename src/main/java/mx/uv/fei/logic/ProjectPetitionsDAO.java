package mx.uv.fei.logic;

import mx.uv.fei.dao.IProjectPetitions;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectPetitionsDAO implements IProjectPetitions {
    @Override
    public List<ProjectPetitions> getProjectPetitions() throws SQLException {
        List<ProjectPetitions> listPetitions = new ArrayList<>();
        String query = "SELECT * FROM SolicitudesProyecto";
        DatabaseManager dataBaseManager = new DatabaseManager();
        Connection connection = dataBaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        do {

            ProjectPetitions objectPetitions = new ProjectPetitions();

            objectPetitions.setProjectPetitionID(results.getInt("ID_solicitudProyecto"));
            objectPetitions.setProjectID(results.getInt("ID_proyecto"));
            objectPetitions.setStudentTuition(results.getString("matriculaEstudiante"));
            objectPetitions.setStatus(results.getString("estado"));
            objectPetitions.setDescription(results.getString("motivos"));

            listPetitions.add(objectPetitions);
        } while (results.next());
        statement.executeUpdate();
        dataBaseManager.closeConnection();
        return listPetitions;
    }

    @Override
    public void createProjectPetition(ProjectPetitions projectPetition) throws SQLException {
        String query = "INSERT INTO SolicitudesProyecto(ID_proyecto, matriculaEstudiante, motivos) VALUES(?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setInt(1, projectPetition.getProjectID());
        statement.setString(2, projectPetition.getStudentTuition());
        statement.setString(3, projectPetition.getDescription());
        statement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public void validateProjectPetition(String option, int projectID, String studentTuition) throws SQLException {
        String query = "UPDATE SolicitudesProyecto SET estado=(?) WHERE ID_proyecto=(?) AND matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, option);
        statement.setInt(2, projectID);
        statement.setString(3, studentTuition);

        statement.executeUpdate();
        databaseManager.closeConnection();
    }

}
