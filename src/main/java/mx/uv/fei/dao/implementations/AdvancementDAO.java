package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IAdvancement;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Advancement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdvancementDAO implements IAdvancement {
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
    public Advancement getAdvancementDetailByName(String advancementName) throws SQLException {
        String query = "select nombre, descripcion, fechaInicio, fechaEntrega from Avances where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        
        Advancement advancementDetail = null;
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, advancementName);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            advancementDetail = new Advancement();
            if (resultSet.next()) {
                advancementDetail.setAdvancementName(resultSet.getString("nombre"));
                advancementDetail.setAdvancementDescription(resultSet.getString("descripcion"));
                advancementDetail.setAdvancementStartDate(resultSet.getString("fechaInicio"));
                advancementDetail.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
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
        String query = "SELECT A.nombre FROM Avances A INNER JOIN Proyectos P on A.ID_proyecto = P.ID_proyecto INNER JOIN Profesores P2 on P.ID_director = P2.ID_profesor WHERE P2.ID_profesor = ?";
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
        String sqlQuery = "SELECT A.nombre FROM Avances A INNER JOIN Proyectos P ON A.ID_proyecto = P.ID_proyecto INNER JOIN ProyectosEstudiantes PE ON PE.ID_proyecto = P.ID_proyecto WHERE PE.matriculaEstudiante = (?)";
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
    public int modifyAdvancementByName(String advancementName, Advancement advancement) throws SQLException {
        String query = "update Avances set nombre=(?), descripcion=(?), fechaInicio=(?), fechaEntrega=(?), ID_proyecto=(?) where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProjectId());
        preparedStatement.setString(6, advancementName);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    @Override
    public int deleteAdvancementByName(String advancementName) throws SQLException {
        String query = "delete from Avances where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancementName);
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
}
