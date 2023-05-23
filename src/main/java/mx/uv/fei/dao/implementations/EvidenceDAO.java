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

/**
 * Provides a set of functions involving manipulation of evidences in the database as well as consulting information.
 */
public class EvidenceDAO implements IEvidence {
    /**
     * @param evidence new evidence to save in the database
     * @return rows affected if the admin was saved (1) or not (0)
     * @throws SQLException if there was an error with the database.
     */
    @Override
    public int addEvidence(Evidence evidence) throws SQLException {
        String query = "insert into Evidencias(titulo, descripcion, ID_avance, matriculaEstudiante, fechaEntrega) values (?,?,?,?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, evidence.getEvidenceTitle());
        preparedStatement.setString(2, evidence.getEvidenceDescription());
        preparedStatement.setInt(3, evidence.getAdvancementId());
        preparedStatement.setString(4, evidence.getStudentId());
        preparedStatement.setString(5, java.time.LocalDate.now().toString());
        int result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param evidence evidence object to modify
     * @return rows affected if the admin was saved (1) or not (0)
     * @throws SQLException if there was an error connecting to the database
     */
    @Override
    public int modifyEvidence(Evidence evidence) throws SQLException{
        int result;
        String query = "UPDATE Evidencias SET titulo=(?), descripcion=(?), fechaEntrega=(?) WHERE ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, evidence.getEvidenceTitle());
        preparedStatement.setString(2, evidence.getEvidenceDescription());
        preparedStatement.setString(3, java.time.LocalDate.now().toString());
        preparedStatement.setInt(4, evidence.getEvidenceId());
        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param id id of evidence to update
     * @param grade new grade to set
     * @throws SQLException if there was an error connecting with to the database
     */
    @Override
    public int updateEvidenceGradeById(int id, int grade) throws SQLException {
        int result;
        String query = "update Evidencias set calificacion=(?), estado=(?) where ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, grade);
        preparedStatement.setString(2, "Revisado");
        preparedStatement.setInt(3, id);

        result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param professorID the id of the professor you want to get their evidences
     * @return list containing ALL evidences assigned to the professor
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public List<Evidence> getEvidenceListByProfessorID(int professorID) throws SQLException {
        String query = "SELECT * FROM Evidencias " +
                "INNER JOIN Avances on Evidencias.ID_avance = Avances.ID_avance " +
                "INNER JOIN Proyectos on Avances.ID_proyecto = Proyectos.ID_proyecto " +
                "WHERE Proyectos.ID_director = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, professorID);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        List<Evidence> evidenceList = new ArrayList<>();
        while (resultSet.next()) {
            Evidence evidence = new Evidence();
            evidence.setEvidenceId(resultSet.getInt("ID_evidencia"));
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            evidence.setEvidenceStatus(resultSet.getString("estado"));
            evidence.setEvidenceGrade(resultSet.getInt("calificacion"));
            evidence.setEvidenceDescription(resultSet.getString("descripcion"));
            evidence.setAdvancementId(resultSet.getInt("ID_avance"));
            evidence.setStudentId(resultSet.getString("matriculaEstudiante"));
            evidenceList.add(evidence);
        }

        return evidenceList;
    }

    /**
     * @param evidenceID the evidence title of the evidence you want to get the whole object of
     * @return the evidence object
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    public Evidence getEvidenceByEvidenceID(int evidenceID) throws SQLException {
        String query = "SELECT ID_evidencia, titulo, estado, calificacion, descripcion FROM Evidencias WHERE ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, evidenceID);
        ResultSet resultSet = preparedStatement.executeQuery();
        databaseManager.closeConnection();

        Evidence evidence = new Evidence();
        while (resultSet.next()) {
            evidence.setEvidenceId(resultSet.getInt("ID_evidencia"));
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            evidence.setEvidenceStatus(resultSet.getString("estado"));
            evidence.setEvidenceGrade(resultSet.getInt("calificacion"));
            evidence.setEvidenceDescription(resultSet.getString("descripcion"));
        }
        return evidence;
    }

    /**
     * @param evidenceID the evidence title you want to get the id from
     * @return integer with the evidence id
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public int getAdvancementIDByEvidenceID(int evidenceID) throws SQLException {
        String query = "SELECT ID_avance FROM Evidencias WHERE ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, evidenceID);
        ResultSet resultSet = preparedStatement.executeQuery();
        int result = 0;
        while (resultSet.next()) {
            result = resultSet.getInt("ID_avance");
        }

        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param evidenceID the evidence title you want to get the student id from
     * @return the id of the student that created the evidence
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public String getStudentIDByEvidenceID(int evidenceID) throws SQLException {
        String query = "SELECT matriculaEstudiante FROM Evidencias WHERE ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, evidenceID);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("matriculaEstudiante");
        }

        databaseManager.closeConnection();

        return result;
    }

    public int deleteEvidenceByID (int evidenceID) throws SQLException {
        String query = "delete from Evidencias where ID_evidencia=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, evidenceID);
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

    @Override
    public List<Evidence> getEvidenceListByStudent(String studentID) throws SQLException {
        String query = "SELECT ID_evidencia, titulo, estado FROM Evidencias WHERE matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Evidence> evidenceList = new ArrayList<>();
        while (resultSet.next()) {
            Evidence evidence = new Evidence();
            evidence.setEvidenceId(resultSet.getInt("ID_evidencia"));
            evidence.setEvidenceTitle(resultSet.getString("titulo"));
            evidence.setEvidenceStatus(resultSet.getString("estado"));
            evidenceList.add(evidence);
        }

        return evidenceList;
    }

    @Override
    public String getAdvancementNameByStudentID(String studentID, int advancementID) throws SQLException {
        String query = "SELECT Avances.nombre FROM Evidencias " +
                "INNER JOIN Avances on Evidencias.ID_avance = Avances.ID_avance " +
                "WHERE matriculaEstudiante = (?) AND Evidencias.ID_avance = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, studentID);
        preparedStatement.setInt(2, advancementID);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()) {
            result = resultSet.getString("nombre");
        }

        databaseManager.closeConnection();

        return result;
    }
    
    @Override
    public List<Evidence> getDeliveredEvidences(String studentID) {
        String sqlQuery = "SELECT titulo, fechaEntrega FROM Evidencias WHERE matriculaEstudiante = (?)";
        
        DatabaseManager databaseManager = new DatabaseManager();
        
        List<Evidence> deliveredEvidences = null;
        try {
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1,studentID);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            deliveredEvidences = new ArrayList<>();
            while (resultSet.next()) {
                Evidence evidence = new Evidence();
                evidence.setEvidenceTitle(resultSet.getString("titulo"));
                evidence.setDeliverDate(resultSet.getString("fechaEntrega"));
                deliveredEvidences.add(evidence);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //log exception
        }
        return deliveredEvidences;
    }
}
