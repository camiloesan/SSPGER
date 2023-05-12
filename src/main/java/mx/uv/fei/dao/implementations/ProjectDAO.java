package mx.uv.fei.dao.implementations;

import javafx.scene.chart.PieChart;
import mx.uv.fei.dao.contracts.IProject;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.DetailedProject;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements IProject {
    @Override
    public int addProject(Project project) throws SQLException {
        int result = 0;
        String sqlQuery = "INSERT INTO Proyectos (claveCA, nombreProyectoInvestigación, LGAC, lineaInvestigacion, duracionAprox, ID_modalidadTR, nombreTrabajoRecepcional, requisitos, alumnosParticipantes, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, resultadosEsperados, bibliografiaRecomendada) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        try {
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
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }
    
    @Override
    public int setDirectorIDtoProject(Project projectDirectorName) throws SQLException {
        int result = 0;
        String sqlQuery = "UPDATE Proyectos PRY SET PRY.ID_director = (SELECT PRF.ID_profesor FROM Profesores PRF WHERE ? LIKE CONCAT('%',PRF.nombre,'%')) WHERE PRY.nombreTrabajoRecepcional = ?";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, projectDirectorName.getDirectorName());
            preparedStatement.setString(2, projectDirectorName.getReceptionWorkName());
            result = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return result;
    }
    
    @Override
    public int setCodirectorIDtoProject(Project projectCodirectorName) throws SQLException {
        int result = 0;
        String sqlQuery = "UPDATE Proyectos PRY SET PRY.ID_codirector = (SELECT PRF.ID_profesor FROM Profesores PRF WHERE ? LIKE CONCAT('%',PRF.nombre,'%')) WHERE PRY.nombreTrabajoRecepcional = ?";
        
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
    public int getProjectIDByTitle(String title) throws SQLException {
        String query = "SELECT ID_proyecto FROM Proyectos WHERE nombreTrabajoRecepcional = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        while (resultSet.next()) {
            result = resultSet.getInt("ID_proyecto");
        }

        databaseManager.closeConnection();

        return result;
    }

    @Override
    public ArrayList<DetailedProject> getProjectsByState(String projectState) throws SQLException {
        String sqlQuery = "SELECT P.ID_proyecto, P.nombreTrabajoRecepcional, P.estado FROM Proyectos P WHERE estado = ?";

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        ArrayList<DetailedProject> detailedProjects = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,projectState);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            detailedProjects= new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject itemSimpleProject = new DetailedProject();
                
                itemSimpleProject.setProjectID(resultSet.getInt("ID_proyecto"));
                itemSimpleProject.setProjectTitle(resultSet.getString("nombreTrabajoRecepcional"));
                
                detailedProjects.add(itemSimpleProject);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }

        return detailedProjects;
    }
    
    @Override
    public List<DetailedProject> getAllProjects() throws SQLException {
        String sqlQuery = "SELECT ID_proyecto, nombreTrabajoRecepcional FROM Proyectos";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<DetailedProject> projectTitles = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            projectTitles = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject itemProjectTitle = new DetailedProject();
                itemProjectTitle.setProjectID(resultSet.getInt("ID_proyecto"));
                itemProjectTitle.setProjectTitle(resultSet.getString("nombreTrabajoRecepcional"));
                projectTitles.add(itemProjectTitle);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return projectTitles;
    }
    
    @Override
    public List<DetailedProject> getProjectsByRole(int professorID) {
        String sqlQuery = "SELECT ID_proyecto, nombreTrabajoRecepcional FROM Proyectos WHERE ID_codirector = ? OR ID_director = ?";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<DetailedProject> projectTitles = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,professorID);
            preparedStatement.setInt(2,professorID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            projectTitles = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject projectTitle = new DetailedProject();
                projectTitle.setProjectID(resultSet.getInt("ID_proyecto"));
                projectTitle.setProjectTitle(resultSet.getString("nombreTrabajoRecepcional"));
                projectTitles.add(projectTitle);
            }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace(); //log exception
        }
        return projectTitles;
    }
    
    @Override
    public DetailedProject getProjectInfoByID(int projectID) throws SQLException{
        String sqlQuery = "SELECT P.ID_proyecto, CA.nombreCA AS 'Cuerpo Académico', P.nombreProyectoInvestigación, CONCAT(LC.clave, '. ', LC.nombre) AS 'LGAC' , P.lineaInvestigacion, P.duracionAprox, MTR.modalidadTR, P.nombreTrabajoRecepcional, P.requisitos, CONCAT (PRF.grado,' ',PRF.nombre, ' ',PRF.apellidos) AS 'Director', CONCAT (CD.grado,' ',CD.nombre, ' ',CD.apellidos) AS 'Co-director', P.alumnosParticipantes, P.descripcionProyectoInvestigacion, P.descripcionTrabajoRecepcional, P.resultadosEsperados ,P.bibliografiaRecomendada FROM Proyectos P LEFT JOIN CuerpoAcademico CA ON P.claveCA = CA.claveCA JOIN LGAC LC ON P.LGAC = LC.ID_lgac LEFT JOIN ModalidadesTR MTR ON P.ID_modalidadTR = MTR.ID_modalidadTR INNER JOIN Profesores PRF ON P.ID_director = PRF.ID_profesor INNER JOIN Profesores CD ON P.ID_codirector = CD.ID_profesor WHERE P.ID_proyecto = ?";
      
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        DetailedProject detailedProject = null;
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1,projectID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            detailedProject = new DetailedProject();
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

                detailedProject.setCoDirector(resultSet.getString("Co-Director"));
                detailedProject.setNumberStudents(resultSet.getInt("alumnosParticipantes"));
                detailedProject.setInvestigationDescription(resultSet.getString("descripcionProyectoInvestigacion"));
                detailedProject.setReceptionWorkDescription(resultSet.getString("descripcionTrabajoRecepcional"));
                detailedProject.setExpectedResults(resultSet.getString("resultadosEsperados"));
                detailedProject.setBibliography(resultSet.getString("bibliografiaRecomendada"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return detailedProject;
    }
    
    @Override
    public List<String> getLgacList() throws SQLException {
        String sqlQuery = "SELECT CONCAT (clave, '. ', nombre) AS ItemLGAC FROM LGAC";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<String> lgacList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            lgacList = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject lgacItem = new DetailedProject();
                lgacItem.setLgacDescription(resultSet.getString("ItemLGAC"));
                lgacList.add(lgacItem.getLgacDescription());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return lgacList;
    }
    
    @Override
    public List<String> getRWModalitiesList() throws SQLException {
        String sqlQuery = "SELECT modalidadTR FROM ModalidadesTR";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<String> rwModalityList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            rwModalityList = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject rwModalityItem = new DetailedProject();
                rwModalityItem.setReceptionWorkModality(resultSet.getString("modalidadTR"));
                rwModalityList.add(rwModalityItem.getReceptionWorkModality());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return rwModalityList;
    }
    
    @Override
    public List<String> getAcademicBodyIDs() throws SQLException {
        String sqlQuery = "SELECT claveCA FROM CuerpoAcademico";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<String> academicBodyIDList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            academicBodyIDList = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject academicBodyID = new DetailedProject();
                academicBodyID.setAcademicBodyID(resultSet.getString("claveCA"));
                academicBodyIDList.add(academicBodyID.getAcademicBodyID());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return academicBodyIDList;
    }

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

    @Override
    public int deleteProjectByTitle(String title) throws SQLException {
        String query = "DELETE FROM Proyectos WHERE nombreTrabajoRecepcional=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();
        return result;
    }
}
