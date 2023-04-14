package mx.uv.fei.logic;

import mx.uv.fei.dao.IProject;
import mx.uv.fei.dataaccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;

public class ProjectDAO implements IProject{
    public int addProject(Project project) throws SQLException {
        int result;
        String sqlQuery = "INSERT INTO Proyectos (claveCA, nombreProyectoInvestigación, LGAC, lineaInvestigacion, duracionAprox, ID_modalidadTR, nombreProyectoInvestigación, requisitos, ID_director, alumnosParticipantes, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, resultadosEsperados, bibliografiaRecomendada, estado, etapa, NRC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

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
        preparedStatement.setString(8,project.getRequisites());
        preparedStatement.setInt(9,project.getDirectorID());
        preparedStatement.setInt(10,project.getStudentsParticipating());
        preparedStatement.setString(11,project.getInvestigationProjectDescription());
        preparedStatement.setString(12,project.getReceptionWorkDescription());
        preparedStatement.setString(13,project.getExpectedResults());
        preparedStatement.setString(14,project.getRecommendedBibliography());
        preparedStatement.setString(15,project.getState());
        preparedStatement.setString(16,project.getStage());
        preparedStatement.setInt(17,project.getNRC());

        result = preparedStatement.executeUpdate();
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

        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    public ArrayList<SimpleProject> getProjectsByState(String projectState) throws SQLException {
        String sqlQuery = null;
        if (projectState == "Verificado") {
            sqlQuery = "SELECT P.ID_proyecto, P.nombreProyectoInvestigación AS 'Proyecto', CONCAT (P2.nombre, ' ',P2.apellidoPaterno, ' ', P2.apellidoMaterno) AS 'Profesor' FROM Proyectos P INNER JOIN Profesores P2 ON P.ID_director = P2.ID_profesor WHERE P.estado = 'Verificado'";
        } else if (projectState == "Por revisar") {
            sqlQuery = "SELECT P.ID_proyecto, P.nombreProyectoInvestigación AS 'Proyecto', CONCAT (P2.nombre, ' ',P2.apellidoPaterno, ' ', P2.apellidoMaterno) AS 'Profesor' FROM Proyectos P INNER JOIN Profesores P2 ON P.ID_director = P2.ID_profesor WHERE P.estado = 'Por revisar'";
        } else if (projectState == "Declinado") {
            sqlQuery = "SELECT P.ID_proyecto, P.nombreProyectoInvestigación AS 'Proyecto', CONCAT (P2.nombre, ' ',P2.apellidoPaterno, ' ', P2.apellidoMaterno) AS 'Profesor' FROM Proyectos P INNER JOIN Profesores P2 ON P.ID_director = P2.ID_profesor WHERE P.estado = 'Declinado'";
        }

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<SimpleProject> simpleProjects = new ArrayList<>();
        while (resultSet.next()) {
            SimpleProject simpleProject = new SimpleProject();

            simpleProject.setProjectID(resultSet.getInt("ID_proyecto"));
            simpleProject.setProjectName(resultSet.getString("Proyecto"));
            simpleProject.setProfessorFullName(resultSet.getString("Profesor"));

            simpleProjects.add(simpleProject);
        }

        databaseManager.closeConnection();

        return simpleProjects;
    }

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
            detailedProject.setAcademicBody(resultSet.getString("Cuerpo Académico"));
            detailedProject.setInvestigationProject(resultSet.getString("nombreProyectoInvestigación"));
            detailedProject.setLGAC(resultSet.getString("LGAC"));
            detailedProject.setInvestigationLine(resultSet.getString("lineaInvestigacion"));
            detailedProject.setApproxDuration(resultSet.getString("duracionAprox"));
            detailedProject.setRwModality(resultSet.getString("modalidadTR"));
            detailedProject.setReceptionWork(resultSet.getString("nombreTrabajorecepcional"));
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
