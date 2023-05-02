package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IEvidence;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Evidence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvidenceDAO implements IEvidence {
    @Override
    public int addEvidence(Evidence evidence) throws SQLException {
        String query = "insert into Evidencias(titulo, descripcion, ID_avance, matriculaEstudiante) values (?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidence.getEvidenceTitle());
        preparedStatement.setString(2, evidence.getEvidenceDescription());
        preparedStatement.setInt(3, evidence.getAdvancementId());
        preparedStatement.setString(4, evidence.getStudentId());
        int result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public int modifyEvidence(int evidenceID, String evidenceTitle, String evidenceDescription) throws SQLException{
        int result;
        String query = "UPDATE Evidencias SET titulo=(?), descripcion=(?) WHERE ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, evidenceTitle);
        preparedStatement.setString(2, evidenceDescription);
        preparedStatement.setInt(3, evidenceID);
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    @Override
    public void updateEvidenceGradeById(String id, int grade) throws SQLException {
        String query = "update Evidencias set calificacion=(?) where matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, grade);
        preparedStatement.setString(2, id);
        preparedStatement.executeUpdate();

        databaseManager.closeConnection();
    }

    @Override
    public List<Evidence> getEvidenceListByStudentId(String id) throws SQLException {
        String query = "select * from Evidencias where matriculaEstudiante=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Evidence> evidenceList = new ArrayList<>();
        while (resultSet.next()) {
            Evidence evidence = new Evidence();
            evidence.setProjectId(resultSet.getInt("ID_evidencia"));
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            evidence.setEvidenceStatus(resultSet.getString("estado"));
            evidence.setEvidenceGrade(resultSet.getInt("calificacion"));
            evidence.setEvidenceDescription(resultSet.getString("descripcion"));
            evidence.setProfessorId(resultSet.getInt("ID_profesor"));
            evidence.setAdvancementId(resultSet.getInt("ID_avance"));
            evidence.setProjectId(resultSet.getInt("ID_proyecto"));
            evidence.setStudentId(resultSet.getString("matriculaEstudiante"));
            evidenceList.add(evidence);
        }

        return evidenceList;
    }

    @Override
    public Evidence getEvidenceByEvidenceTitle(String evidenceTitle) throws SQLException {
        String query = "SELECT titulo, estado, calificacion, descripcion FROM Evidencias WHERE titulo=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidenceTitle);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        Evidence evidence = new Evidence();
        while (resultSet.next()) {
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            evidence.setEvidenceStatus(resultSet.getString("estado"));
            evidence.setEvidenceGrade(resultSet.getInt("calificacion"));
            evidence.setEvidenceDescription(resultSet.getString("descripcion"));
        }
        return evidence;
    }
    @Override
    public int getAdvancementIDByEvidenceTitle(String evidenceTitle) throws SQLException {
        String query = "SELECT ID_avance FROM Evidencias WHERE titulo=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidenceTitle);
        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        while (resultSet.next()) {
            result = resultSet.getInt("ID_avance");
        }

        databaseManager.closeConnection();

        return result;
    }
    @Override
    public String getStudentIDByEvidenceTitle(String evidenceTitle) throws SQLException {
        String query = "SELECT matriculaEstudiante FROM Evidencias WHERE titulo=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidenceTitle);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("matriculaEstudiante");
        }

        databaseManager.closeConnection();

        return result;
    }

    public int deleteEvidenceByName(String evidenceName) throws SQLException {
        String query = "delete from Evidencias where titulo=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidenceName);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    public List<Evidence> getListEvidenceName() throws SQLException {
        String query = "SELECT titulo FROM Evidencias";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Evidence> advancementNameList = new ArrayList<>();
        while(resultSet.next()) {
            Evidence evidence = new Evidence();
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            advancementNameList.add(evidence);
        }

        return advancementNameList;
    }
}
