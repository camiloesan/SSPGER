package mx.uv.fei.dao;

import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Advancement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdvancementDAO implements IAdvancement {
    @Override
    public int addAdvancement(Advancement advancement) throws SQLException {
        String query = "insert into Avances(nombre, descripcion, fechaInicio, fechaEntrega, ID_profesor, ID_proyecto) values (?,?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProfessorId());
        preparedStatement.setInt(6, advancement.getProjectId());
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    @Override
    public List<Advancement> getAdvancementDetailByName(String advancementName) throws SQLException {
        String query = "select * from Avances where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancementName);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementDetail = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementDescription(resultSet.getString("descripcion"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancement.setProfessorId(resultSet.getInt("ID_profesor"));
            advancement.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementDetail.add(advancement);
        }

        return advancementDetail;
    }

    @Override
    public List<Advancement> getAdvancementListByProjectName(String projectName) throws SQLException {
        String query = "select nombre, fechaInicio, fechaEntrega from Avances where ID_proyecto";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Advancement> advancementList = new ArrayList<>();
        while (resultSet.next()) {
            Advancement advancement = new Advancement();
            advancement.setAdvancementName(resultSet.getString("nombre"));
            advancement.setAdvancementDescription(resultSet.getString("descripcion"));
            advancement.setAdvancementStartDate(resultSet.getString("fechainicio"));
            advancement.setAdvancementDeadline(resultSet.getString("fechaEntrega"));
            advancement.setProfessorId(resultSet.getInt("ID_profesor"));
            advancement.setProjectId(resultSet.getInt("ID_proyecto"));
            advancementList.add(advancement);
        }

        return advancementList;
    }

    @Override
    public int modifyAdvancementByName(String advancementName, Advancement advancement) throws SQLException {
        String query = "update Avances set nombre=(?), descripcion=(?), fechaInicio=(?), fechaEntrega=(?), ID_profesor=(?), ID_proyecto=(?) where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, advancement.getAdvancementName());
        preparedStatement.setString(2, advancement.getAdvancementDescription());
        preparedStatement.setString(3, advancement.getAdvancementStartDate());
        preparedStatement.setString(4, advancement.getAdvancementDeadline());
        preparedStatement.setInt(5, advancement.getProfessorId());
        preparedStatement.setInt(6, advancement.getProjectId());
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
    public int getProfessorIdByUsername(String username) throws SQLException {
        String query = "select ID_profesor from Profesores where nombre=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }
}
