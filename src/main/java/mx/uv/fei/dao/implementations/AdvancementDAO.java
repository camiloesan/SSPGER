package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IAdvancement;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Advancement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a set of methods to connect to the advancements table on the database.
 */
public class AdvancementDAO implements IAdvancement {
    /**
     * @param advancement new advancement
     * @return rows affected, if the advancement was added or not.
     * @throws SQLException if there was a problem with the database.
     */
    @Override
    public int addAdvancement(Advancement advancement) throws SQLException {
        String query = "insert into Avances(nombre, descripcion, fechaInicio, fechaEntrega, ID_proyecto) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProjectId());
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param advancementId id of the advancement you want to get details from 
     * @return an object Advancement with all its information
     * @throws SQLException if there was an error on the database
     */
    @Override
    public Advancement getAdvancementDetailById(int advancementId) throws SQLException {
        String query = "select nombre, descripcion, fechaInicio, fechaEntrega, ID_avance from Avances where ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        Advancement advancementDetail = null;
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, advancementId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            advancementDetail = new Advancement();
            if (resultSet.next()) {
                advancementDetail.setAdvancementName(resultSet.getString("nombre"));
                advancementDetail.setAdvancementDescription(resultSet.getString("descripcion"));
                advancementDetail.setAdvancementStartDate(resultSet.getString("fechaInicio"));
                advancementDetail.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
                advancementDetail.setAdvancementID(resultSet.getInt("ID_avance"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }

        return advancementDetail;
    }

    /**
     * @param projectId project id you want to get all the advancements from
     * @return a list containing the advancements of the given project as a list
     * @throws SQLException if there was an error connecting to the database.
     */
    public List<Advancement> getAdvancementListByProjectId(int projectId) throws SQLException {
        String query = "select nombre, descripcion, fechaInicio, fechaEntrega, ID_avance, ID_proyecto from Avances where ID_proyecto=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, projectId);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementDescription(resultSet.getString("descripcion"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancement.setAdvancementID(resultSet.getInt("ID_avance"));
            advancement.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    /**
     * @param professorID id of the professor you want to get their advancements
     * @return List containing ALL advancements corresponding to the requested professor
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public ArrayList<Advancement> getListAdvancementNamesByProfessorId(int professorID) throws SQLException {
        String query = "SELECT A.nombre, A.fechaInicio, A.fechaEntrega, A.ID_avance FROM Avances A INNER JOIN Proyectos P on A.ID_proyecto = P.ID_proyecto INNER JOIN Profesores P2 on P.ID_director = P2.ID_profesor WHERE P2.ID_profesor = ?";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        ArrayList<Advancement> advancementNameList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,professorID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            advancementNameList = new ArrayList<>();
            while(resultSet.next()) {
                Advancement advancement = new Advancement();
                advancement.setAdvancementName(resultSet.getString("nombre"));
                advancement.setAdvancementStartDate(resultSet.getString("fechaInicio"));
                advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
                advancement.setAdvancementID(resultSet.getInt("ID_avance"));
                advancementNameList.add(advancement);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        
        return advancementNameList;
    }

    /**
     * @param studentID id of the student you want to get their advancements
     * @return list of advancements corresponding to a student
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public List<Advancement> getListAdvancementNamesByStudentId(String studentID) throws SQLException {
        String sqlQuery = "SELECT A.nombre FROM Avances A INNER JOIN Proyectos P ON A.ID_proyecto = P.ID_proyecto INNER JOIN SolicitudesProyecto SP on P.ID_proyecto = SP.ID_proyecto WHERE SP.matriculaEstudiante = ? AND SP.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        List<Advancement> advancementList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            advancementList = new ArrayList<>();
            while (resultSet.next()) {
                Advancement advancementItem = new Advancement();
                advancementItem.setAdvancementName(resultSet.getString("nombre"));
                advancementList.add(advancementItem);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return advancementList;
    }

    /**
     * @param advancementId id of the advancements to modify
     * @param advancement the new advancement object to replace the old one
     * @return rows affected (0 or 1) whether the advancement was added or not
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int modifyAdvancementById(int advancementId, Advancement advancement) throws SQLException {
        String query = "update Avances set nombre=(?), descripcion=(?), fechaInicio=(?), fechaEntrega=(?), ID_proyecto=(?) where ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProjectId());
        preparedStatement.setInt(6, advancementId);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param advancementId id of the advancement you want to delete from the database
     * @return rows affected (0 or 1) whether the advancement was deleted or not
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int deleteAdvancementById(int advancementId) throws SQLException {
        String query = "delete from Avances where ID_avance=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, advancementId);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param studentID the student id you want to get their advancements
     * @return list containing ALL the advancements of the given student id
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public List<Advancement> getAdvancementByStudentID(String studentID) throws SQLException {
        String query = "SELECT Avances.ID_avance, Avances.nombre, Avances.fechaInicio, Avances.fechaEntrega FROM Avances " +
                "INNER JOIN Proyectos ON Avances.ID_proyecto = Proyectos.ID_proyecto " +
                "INNER JOIN SolicitudesProyecto ON Proyectos.ID_proyecto = SolicitudesProyecto.ID_proyecto " +
                "WHERE matriculaEstudiante = (?) AND SolicitudesProyecto.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementID(resultSet.getInt("ID_avance"));
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    /**
     * @param studentID the student id you want to get their project name
     * @return the number of rows affected
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public String getProjectNameByStudentID(String studentID) throws SQLException {
        String query = "SELECT Proyectos.nombreTrabajoRecepcional FROM Avances " +
                "INNER JOIN Proyectos ON Avances.ID_proyecto = Proyectos.ID_proyecto " +
                "INNER JOIN SolicitudesProyecto ON Proyectos.ID_proyecto = SolicitudesProyecto.ID_proyecto " +
                "WHERE matriculaEstudiante = (?) AND SolicitudesProyecto.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        String result = null;
        while (resultSet.next()) {
            result = resultSet.getString("nombreTrabajoRecepcional");
        }

        databaseManager.closeConnection();

        return result;
    }

    @Override
    public int getAdvancementIDByAdvancementName(String advancementName) throws SQLException {
        int result = 0;
        String query = "SELECT ID_avance FROM Avances WHERE nombre = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancementName);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            result = resultSet.getInt("ID_avance");
        }

        databaseManager.closeConnection();

        return result;
    }
}
