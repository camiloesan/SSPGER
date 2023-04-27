package mx.uv.fei.dao;

import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.DetailedProject;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectDAO implements IProject{
    @Override
    public int addProject(Project project) throws SQLException {
        int result = 0;
        String sqlQuery = "INSERT INTO Proyectos (claveCA, nombreProyectoInvestigación, LGAC, lineaInvestigacion, duracionAprox, ID_modalidadTR, nombreTrabajoRecepcional, requisitos, ID_director, ID_codirector,alumnosParticipantes, descripcionProyectoInvestigacion, descripcionTrabajoRecepcional, resultadosEsperados, bibliografiaRecomendada) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

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
            preparedStatement.setInt(9, project.getDirectorID());
            preparedStatement.setInt(10, project.getCodirectorID());
            preparedStatement.setInt(11, project.getStudentsParticipating());
            preparedStatement.setString(12, project.getInvestigationProjectDescription());
            preparedStatement.setString(13, project.getReceptionWorkDescription());
            preparedStatement.setString(14, project.getExpectedResults());
            preparedStatement.setString(15, project.getRecommendedBibliography());
            
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
    public ArrayList<DetailedProject> getProjectsByState(String projectState) throws SQLException {
        String sqlQuery = "SELECT P.ID_proyecto, P.nombreTrabajoRecepcional, CONCAT (P2.nombre, ' ',P2.apellidos) AS 'Profesor' FROM Proyectos P INNER JOIN Profesores P2 ON P.ID_director = P2.ID_profesor WHERE P.estado = ?";

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
                itemSimpleProject.setDirector(resultSet.getString("Profesor"));
                
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
        String sqlQuery = "SELECT nombreTrabajoRecepcional FROM Proyectos";
        
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<DetailedProject> projectTitles = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            projectTitles = new ArrayList<>();
            while (resultSet.next()) {
                DetailedProject itemProjectTitle = new DetailedProject();
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
    public DetailedProject getProjectInfo(String projectTitle) throws SQLException{
        String sqlQuery = "SELECT CA.nombreCA AS 'Cuerpo Académico', P.nombreProyectoInvestigación, CONCAT(LC.clave, '. ', LC.nombre) AS 'LGAC' , P.lineaInvestigacion, P.duracionAprox, MTR.modalidadTR, P.nombreTrabajoRecepcional, P.requisitos, CONCAT (PRF.nombre, ' ',PRF.apellidos) AS 'Director', CONCAT (CD.nombre, ' ',CD.apellidos) AS 'Co-director', P.alumnosParticipantes, P.descripcionProyectoInvestigacion, P.descripcionTrabajoRecepcional, P.resultadosEsperados ,P.bibliografiaRecomendada FROM Proyectos P LEFT JOIN CuerpoAcademico CA ON P.claveCA = CA.claveCA JOIN LGAC LC ON P.LGAC = LC.ID_lgac LEFT JOIN ModalidadesTR MTR ON P.ID_modalidadTR = MTR.ID_modalidadTR INNER JOIN Profesores PRF ON P.ID_director = PRF.ID_profesor INNER JOIN Profesores CD ON CD.ID_profesor = P.ID_codirector WHERE P.nombreTrabajoRecepcional LIKE ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        DetailedProject detailedProject = null;
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,projectTitle);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            detailedProject = new DetailedProject();
            if (resultSet.next()) {
                //detailedProject.setProjectID(resultSet.getInt("ID_proyecto"));
                detailedProject.setAcademicBodyName(resultSet.getString("Cuerpo Académico"));
                detailedProject.setInvestigationProjectName(resultSet.getString("nombreProyectoInvestigación"));
                detailedProject.setLgacDescription(resultSet.getString("LGAC"));
                detailedProject.setInvestigationLine(resultSet.getString("lineaInvestigacion"));
                detailedProject.setApproxDuration(resultSet.getString("duracionAprox"));
                detailedProject.setReceptionWorkModality(resultSet.getString("modalidadTR"));
                detailedProject.setReceptionWorkName(resultSet.getString("nombreTrabajorecepcional"));
                detailedProject.setRequisites(resultSet.getString("requisitos"));
                detailedProject.setDirector(resultSet.getString("Director"));
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
    public List<Integer> getNRCs() throws SQLException {
        String sqlQuery = "SELECT NRC FROM ExperienciasEducativas";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<Integer> NRCs = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            NRCs = new ArrayList<>();
            while (resultSet.next()){
                Project nrcItem = new Project();
                nrcItem.setNRC(resultSet.getInt("NRC"));
                NRCs.add(nrcItem.getNRC());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return NRCs;
    }
}
