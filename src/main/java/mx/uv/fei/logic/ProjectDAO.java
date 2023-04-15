package mx.uv.fei.logic;

import mx.uv.fei.dao.IProject;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectDAO implements IProject{
    @Override
    public int addProject(Project project) throws SQLException {
        int result;
        String sqlQuery = "INSERT INTO Proyectos (claveCA, nombreProyectoInvestigación, LGAC, lineaInvestigacion, duracionAprox, ID_modalidadTR, nombreTrabajoRecepcional, requisitos, ID_director, alumnosParticipantes, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, resultadosEsperados, bibliografiaRecomendada, etapa, NRC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1,project.getAcademicBodyId());
        preparedStatement.setString(2,project.getInvestigationProjectName());
        preparedStatement.setString(3,project.getLGAC_Id());
        preparedStatement.setString(4,project.getInvestigationLine());
        preparedStatement.setString(5,project.getApproximateDuration());
        preparedStatement.setInt(6,project.getModalityId());
        preparedStatement.setString(7,project.getReceptionWorkName());
        preparedStatement.setString(8,project.getRequisites());
        preparedStatement.setInt(9,project.getDirectorID());
        preparedStatement.setInt(10,project.getStudentsParticipating());
        preparedStatement.setString(11,project.getInvestigationProjectDescription());
        preparedStatement.setString(12,project.getReceptionWorkDescription());
        preparedStatement.setString(13,project.getExpectedResults());
        preparedStatement.setString(14,project.getRecommendedBibliography());
        preparedStatement.setString(15,project.getStage());
        preparedStatement.setInt(16,project.getNRC());

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }
    
    @Override
    public int updateProjectState(int projectId, String state) throws SQLException {
        int result;
        String sqlQuery = "UPDATE Proyectos SET estado = (?) WHERE ID_proyecto = (?)";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, state);
        preparedStatement.setInt(2,projectId);

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    @Override
    public ArrayList<DetailedProject> getProjectsByState(String projectState) throws SQLException {
        String sqlQuery = "SELECT P.ID_proyecto, P.nombreTrabajoRecepcional AS TrabajoRecepcional, P.nombreProyectoInvestigación AS 'ProyectoInvestigacion', CONCAT (P2.nombre, ' ',P2.apellidoPaterno, ' ', P2.apellidoMaterno) AS 'Profesor' FROM Proyectos P INNER JOIN Profesores P2 ON P.ID_director = P2.ID_profesor WHERE P.estado = ?";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,projectState);

        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<DetailedProject> detailedProjects = new ArrayList<>();
        while (resultSet.next()) {
            DetailedProject detailedProject = new DetailedProject();

            detailedProject.setProjectID(resultSet.getInt("ID_proyecto"));
            if (resultSet.getString("ProyectoInvestigacion") == null || Objects.equals(resultSet.getString("ProyectoInvestigacion"), " ")){
                detailedProject.setProjectTitle(resultSet.getString("TrabajoRecepcional"));
            } else {
                detailedProject.setProjectTitle(resultSet.getString("ProyectoInvestigacion"));
            }
            detailedProject.setDirector(resultSet.getString("Profesor"));

            detailedProjects.add(detailedProject);
        }

        databaseManager.closeConnection();

        return detailedProjects;
    }

    @Override
    public DetailedProject getProjectInfo(int projectID) throws SQLException{
        String sqlQuery = "SELECT P.ID_proyecto, CA.nombreCA AS 'Cuerpo Académico', P.nombreProyectoInvestigación, LC.nombre AS 'LGAC' , P.lineaInvestigacion, P.duracionAprox, MTR.modalidadTR, P.nombreTrabajoRecepcional, P.requisitos, CONCAT (PRF.nombre, ' ',PRF.apellidoPaterno, ' ', PRF.apellidoMaterno) AS 'Director', CONCAT (CD.nombre, ' ',CD.apellidoPaterno, ' ', CD.apellidoMaterno) AS 'Co-director', P.alumnosParticipantes, P.descripcionProyectoInvestigacion, P.descripcionTrabajoRecepcional, P.resultadosEsperados ,P.bibliografiaRecomendada FROM Proyectos P LEFT JOIN CuerpoAcademico CA ON P.claveCA = CA.claveCA JOIN LGAC LC ON P.LGAC = LC.clave LEFT JOIN ModalidadesTR MTR ON P.ID_modalidadTR = MTR.ID_modalidadTR LEFT JOIN Profesores PRF ON P.ID_director = PRF.ID_profesor LEFT JOIN CodirectoresProyecto COP ON P.ID_proyecto = COP.ID_proyecto LEFT JOIN Profesores CD ON COP.ID_profesor = CD.ID_profesor WHERE P.ID_proyecto = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1,projectID);

        ResultSet resultSet = preparedStatement.executeQuery();

        DetailedProject detailedProject = new DetailedProject();
        if (resultSet.next()) {
            detailedProject.setProjectID(resultSet.getInt("ID_proyecto"));
            detailedProject.setAcademicBodyName(resultSet.getString("Cuerpo Académico"));
            detailedProject.setInvestigationProjectName(resultSet.getString("nombreProyectoInvestigación"));
            detailedProject.setLgacDescription(resultSet.getString("LGAC"));
            detailedProject.setInvestigationLine(resultSet.getString("lineaInvestigacion"));
            detailedProject.setApproxDuration(resultSet.getString("duracionAprox"));
            detailedProject.setReceptionWorkModality(resultSet.getString("modalidadTR"));
            detailedProject.setReceptionWorkName(resultSet.getString("nombreTrabajorecepcional"));
            detailedProject.setRequisites(resultSet.getString("requisitos"));
            detailedProject.setDirector(resultSet.getString("Director"));
            detailedProject.setCoDirector(resultSet.getString("Co-director"));
            detailedProject.setNumberStudents(resultSet.getInt("alumnosParticipantes"));
            detailedProject.setInvestigationDescription(resultSet.getString("descripcionProyectoInvestigacion"));
            detailedProject.setReceptionWorkDescription(resultSet.getString("descripcionTrabajoRecepcional"));
            detailedProject.setExpectedResults(resultSet.getString("resultadosEsperados"));
            detailedProject.setBibliography(resultSet.getString("bibliografiaRecomendada"));
        }

        databaseManager.closeConnection();

        return detailedProject;
    }
}
