package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IAdvancement;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Advancement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Advancement> getAdvancementListByProjectId(int projectId) throws SQLException {
        String query = "select nombre, descripcion, fechaInicio, fechaEntrega, ID_proyecto from Avances where ID_proyecto=(?)";
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
            advancement.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    @Override
    public ArrayList<Advancement> getListAdvancementName(int professorID) throws SQLException {
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
    
    @Override
    public List<Advancement> getListAdvancementNameStudent(String studentID) throws SQLException {
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

    @Override
    public String getAdvancementNameByID(int id) throws SQLException {
        String query = "SELECT nombre FROM Avances WHERE ID_avance = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("nombre");
        }

        databaseManager.closeConnection();

        return result;
    }

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

    @Override
    public int getAdvancementIDByStudentID(String studentID) throws SQLException {
        String query = "SELECT Avances.ID_proyecto FROM Avances " +
                "INNER JOIN Proyectos ON Avances.ID_proyecto = Proyectos.ID_proyecto " +
                "INNER JOIN SolicitudesProyecto ON Proyectos.ID_proyecto = SolicitudesProyecto.ID_proyecto " +
                "WHERE matriculaEstudiante = (?) AND SolicitudesProyecto.estado = 'Aceptado'";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        while (resultSet.next()) {
            result = resultSet.getInt("ID_proyecto");
        }

        databaseManager.closeConnection();

        return result;
    }

}
