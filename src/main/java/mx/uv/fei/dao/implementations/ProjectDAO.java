package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IProject;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.SimpleProject;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements IProject {
    /**
     * @param project project to register in the database
     * @return number of affected rows
     * @throws SQLException if there was problem connecting to the database or adding the project
     */
    @Override
    public int addProject(Project project) throws SQLException {
        int result;
        String sqlQuery = "INSERT INTO Proyectos (claveCA, nombreProyectoInvestigación, LGAC, lineaInvestigacion, " +
                "duracionAprox, ID_modalidadTR, nombreTrabajoRecepcional, requisitos, alumnosParticipantes, " +
                "descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, resultadosEsperados, " +
                "bibliografiaRecomendada) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        
        preparedStatement.setString(1, project.getAcademicBodyId());
        preparedStatement.setString(2, project.getInvestigationProjectName());
        preparedStatement.setInt(3, project.getLGAC_Id());
        preparedStatement.setString(4, project.getInvestigationLine());
        preparedStatement.setString(5, project.getApproximateDuration());
        preparedStatement.setInt(6, project.getModalityId());
        preparedStatement.setString(7, project.getReceptionWorkName());
        preparedStatement.setString(8, project.getRequisites());
        preparedStatement.setInt(9, project.getStudentsParticipating());
        preparedStatement.setString(10, project.getInvestigationProjectDescription());
        preparedStatement.setString(11, project.getReceptionWorkDescription());
        preparedStatement.setString(12, project.getExpectedResults());
        preparedStatement.setString(13, project.getRecommendedBibliography());
        
        result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        
        return result;
    }
    
    /**
     * @param projectDirectorName project to set its director
     * @return number of affected rows
     * @throws SQLException if there was a problem connecting to the database or updating the project
     */
    @Override
    public int setDirectorIDtoProject(Project projectDirectorName) throws SQLException {
        int result;
        String sqlQuery = "UPDATE Proyectos PRY SET PRY.ID_director = (SELECT PRF.ID_profesor FROM Profesores PRF " +
                "WHERE ? LIKE CONCAT('%',PRF.nombre,'%')) WHERE PRY.nombreTrabajoRecepcional = ?";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, projectDirectorName.getDirectorName());
        preparedStatement.setString(2, projectDirectorName.getReceptionWorkName());
        result = preparedStatement.executeUpdate();
        
        databaseManager.closeConnection();
        return result;
    }
    
    /**
     * @param projectCodirectorName project to set its codirector
     * @return number of affected rows
     * @throws SQLException if there was a problem connecting to the database or updating the project
     */
    @Override
    public int setCodirectorIDtoProject(Project projectCodirectorName) throws SQLException {
        int result = 0;
        String sqlQuery = "UPDATE Proyectos PRY SET PRY.ID_codirector = (SELECT PRF.ID_profesor FROM Profesores PRF " +
                "WHERE (?) LIKE CONCAT('%',PRF.nombre,'%')) WHERE PRY.nombreTrabajoRecepcional = ?";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, projectCodirectorName.getCodirectorName());
            preparedStatement.setString(2, projectCodirectorName.getReceptionWorkName());
            result = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }
    
    /**
     * @param projectId the id of a project to update
     * @param state new state
     * @return number of affected rows
     * @throws SQLException if there was a problem connecting to the database or updating the project
     */
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
    
    /**
     * @param title project title to get its id
     * @return the id of a registered project
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public int getProjectIDByTitle(String title) throws SQLException {
        String query = "SELECT ID_proyecto FROM Proyectos WHERE nombreTrabajoRecepcional = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet resultSet = preparedStatement.executeQuery();
        int projectId = 0;
        while (resultSet.next()) {
            projectId = resultSet.getInt("ID_proyecto");
        }

        databaseManager.closeConnection();
        return projectId;
    }
    
    /**
     * @param projectState state to get the projects
     * @return List of simple projects by state
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<SimpleProject> getProjectsByState(String projectState) throws SQLException {
        String sqlQuery = "SELECT P.ID_proyecto, P.nombreTrabajoRecepcional, P.estado FROM Proyectos P " +
                "WHERE estado = ?";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1,projectState);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        ArrayList<SimpleProject> simpleProjects = new ArrayList<>();
        while (resultSet.next()) {
            SimpleProject itemSimpleProject = new SimpleProject();
            
            itemSimpleProject.setProjectID(resultSet.getInt("ID_proyecto"));
            itemSimpleProject.setReceptionWorkName(resultSet.getString("nombreTrabajoRecepcional"));
            itemSimpleProject.setProjectState(resultSet.getString("estado"));
            
            simpleProjects.add(itemSimpleProject);
        }
        
        databaseManager.closeConnection();
        return simpleProjects;
    }
    
    /**
     * @return List with all registered projects
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<SimpleProject> getAllProjects() throws SQLException {
        String sqlQuery = "SELECT ID_proyecto, nombreTrabajoRecepcional, estado FROM Proyectos";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<SimpleProject> allProjects = new ArrayList<>();
        while (resultSet.next()) {
            SimpleProject itemProjectTitle = new SimpleProject();
            itemProjectTitle.setProjectID(resultSet.getInt("ID_proyecto"));
            itemProjectTitle.setReceptionWorkName(resultSet.getString("nombreTrabajoRecepcional"));
            itemProjectTitle.setProjectState(resultSet.getString("estado"));
            allProjects.add(itemProjectTitle);
        }
        databaseManager.closeConnection();
        
        return allProjects;
    }
    
    /**
     * @param professorID profesor id to get their projects
     * @return list of simple projects by director
     * @throws SQLException  if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<SimpleProject> getProjectsByParticipation(int professorID) throws SQLException {
        String sqlQuery = "SELECT ID_proyecto, nombreTrabajoRecepcional, estado FROM Proyectos " +
                "WHERE ID_codirector = (?) OR ID_director = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setInt(1,professorID);
        preparedStatement.setInt(2,professorID);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<SimpleProject> projectTitles = new ArrayList<>();
        while (resultSet.next()) {
            SimpleProject projectTitle = new SimpleProject();
            projectTitle.setProjectID(resultSet.getInt("ID_proyecto"));
            projectTitle.setReceptionWorkName(resultSet.getString("nombreTrabajoRecepcional"));
            projectTitle.setProjectState(resultSet.getString("estado"));
            projectTitles.add(projectTitle);
        }
        
        databaseManager.closeConnection();
        return projectTitles;
    }
    
    /**
     * @param projectID the id of a registered project
     * @return project with all its details
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public DetailedProject getProjectInfoByID(int projectID) throws SQLException{
        String sqlQuery = "SELECT P.ID_proyecto, CA.nombreCA AS 'Cuerpo Académico', P.nombreProyectoInvestigación, " +
                "CONCAT(LC.clave, '. ', LC.nombre) AS 'LGAC' , P.lineaInvestigacion, P.duracionAprox, MTR.modalidadTR, " +
                "P.nombreTrabajoRecepcional, P.requisitos, CONCAT (PRF.grado,' ',PRF.nombre, ' ',PRF.apellidos) " +
                "AS 'Director', CONCAT (CD.grado,' ',CD.nombre, ' ',CD.apellidos) " +
                "AS 'Co-director', P.alumnosParticipantes, P.descripcionProyectoInvestigacion, " +
                "P.descripcionTrabajoRecepcional, P.resultadosEsperados ,P.bibliografiaRecomendada, P.estado " +
                "FROM Proyectos P LEFT JOIN CuerpoAcademico CA ON P.claveCA = CA.claveCA " +
                "JOIN LGAC LC ON P.LGAC = LC.ID_lgac " +
                "LEFT JOIN ModalidadesTR MTR ON P.ID_modalidadTR = MTR.ID_modalidadTR " +
                "INNER JOIN Profesores PRF ON P.ID_director = PRF.ID_profesor " +
                "INNER JOIN Profesores CD ON P.ID_codirector = CD.ID_profesor WHERE P.ID_proyecto = ?";
      
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
            detailedProject.setLgacName(resultSet.getString("LGAC"));
            detailedProject.setInvestigationLine(resultSet.getString("lineaInvestigacion"));
            detailedProject.setApproxDuration(resultSet.getString("duracionAprox"));
            detailedProject.setReceptionWorkModality(resultSet.getString("modalidadTR"));
            detailedProject.setReceptionWorkName(resultSet.getString("nombreTrabajorecepcional"));
            detailedProject.setRequisites(resultSet.getString("requisitos"));
            detailedProject.setDirector(resultSet.getString("Director"));
            detailedProject.setCoDirector(resultSet.getString("Co-Director"));
            detailedProject.setNumberStudents(resultSet.getInt("alumnosParticipantes"));
            detailedProject.setInvestigationDescription(resultSet.getString(
                    "descripcionProyectoInvestigacion"));
            detailedProject.setReceptionWorkDescription(resultSet.getString(
                    "descripcionTrabajoRecepcional"));
            detailedProject.setExpectedResults(resultSet.getString("resultadosEsperados"));
            detailedProject.setBibliography(resultSet.getString("bibliografiaRecomendada"));
            detailedProject.setProjectState(resultSet.getString("estado"));
        }
        
        databaseManager.closeConnection();
        return detailedProject;
    }
    
    /**
     * @return list with LGAC
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<String> getLgacList() throws SQLException {
        String sqlQuery = "SELECT CONCAT (clave, '. ', nombre) AS ItemLGAC FROM LGAC";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<String> lgacList = new ArrayList<>();
        while (resultSet.next()) {
            DetailedProject lgacItem = new DetailedProject();
            lgacItem.setLgacName(resultSet.getString("ItemLGAC"));
            lgacList.add(lgacItem.getLgacName());
        }
        
        databaseManager.closeConnection();
        return lgacList;
    }
    
    /**
     * @return list with reception work modalities
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<String> getRWModalitiesList() throws SQLException {
        String sqlQuery = "SELECT modalidadTR FROM ModalidadesTR";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<String> rwModalityList = new ArrayList<>();
        while (resultSet.next()) {
            DetailedProject rwModalityItem = new DetailedProject();
            rwModalityItem.setReceptionWorkModality(resultSet.getString("modalidadTR"));
            rwModalityList.add(rwModalityItem.getReceptionWorkModality());
        }

        databaseManager.closeConnection();
        return rwModalityList;
    }
    
    /**
     * @return list with academic bodies IDs
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<String> getAcademicBodyIDs() throws SQLException {
        String sqlQuery = "SELECT claveCA FROM CuerpoAcademico";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        List<String> academicBodyIDList = new ArrayList<>();
        while (resultSet.next()) {
            DetailedProject academicBodyID = new DetailedProject();
            academicBodyID.setAcademicBodyID(resultSet.getString("claveCA"));
            academicBodyIDList.add(academicBodyID.getAcademicBodyID());
        }

        databaseManager.closeConnection();
        return academicBodyIDList;
    }
    
    /**
     * @param directorId
     * @return list with reception works names
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public List<String> getProjectNamesByIdDirector(int directorId) throws SQLException {
        String query = "select nombreTrabajoRecepcional from Proyectos where ID_director=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, directorId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> projectNamesList = new ArrayList<>();
        while (resultSet.next()) {
            projectNamesList.add(resultSet.getString("nombreTrabajoRecepcional"));
        }
        
        databaseManager.closeConnection();
        return projectNamesList;
    }
    
    /**
     * @param projectId
     * @return project name
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public String getProjectNameById(int projectId) throws SQLException {
        String query = "select nombreTrabajoRecepcional from Proyectos where ID_proyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, projectId);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        String projectName = "";
        while (resultSet.next()) {
            projectName = resultSet.getString("nombreTrabajoRecepcional");
        }

        return projectName;
    }
    
    /**
     * @param projectID the id of a registered project
     * @return number of affected rows
     * @throws SQLException if there was a problem connecting to the database or deleting the project
     */
    @Override
    public int deleteProjectByID(int projectID) throws SQLException {
        String query = "DELETE FROM Proyectos WHERE ID_proyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, projectID);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        return result;
    }
    
    /**
     * @param projectTitle project title to search for coincidences
     * @return true if there is a registered project with the specified title, false if there is not
     * @throws SQLException if there was a problem connecting to the database or getting the information
     */
    @Override
    public boolean isProjectRegistered(String projectTitle) throws SQLException {
        boolean flag = false;
        
        String sqlQuery = "SELECT COUNT(nombreTrabajoRecepcional) AS registeredProjects FROM Proyectos WHERE nombreTrabajoRecepcional = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, projectTitle);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
            int registeredProjects = resultSet.getInt("registeredProjects");
            if (registeredProjects > 0) {
                flag = true;
            }
        }
        
        resultSet.close();
        preparedStatement.close();
        databaseManager.closeConnection();
        
        return flag;
    }
}
