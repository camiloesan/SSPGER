package mx.uv.fei.dao.implementations;

import mx.uv.fei.dao.contracts.IFeedback;
import mx.uv.fei.dataaccess.DatabaseManager;
import mx.uv.fei.logic.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackDAO implements IFeedback {
    /**
     * @param feedback new feedback to save in the database.
     * @return rows affected if the admin was saved (1) or not (0).
     * @throws SQLException if there was an error with the database.
     */
    @Override
    public int addFeedback(Feedback feedback) throws SQLException {
        String query = "INSERT INTO Retroalimentacion (ID_evidencia, textoRetroalimentacion) VALUES (?,?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, feedback.getEvidenceID());
        preparedStatement.setString(2, feedback.getFeedbackText());

        int result = preparedStatement.executeUpdate();

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param feedbackID the feedback ID you want to delete.
     * @return rows affected if the admin was saved (1) or not (0).
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public int deleteFeedbackByID(int feedbackID) throws SQLException {
        String query = "DELETE FROM Retroalimentacion WHERE ID_retroalimentacion=(?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, feedbackID);
        int result = preparedStatement.executeUpdate();
        databaseManager.closeConnection();

        return result;
    }

    /**
     * @param evidenceID the evidence identification to get Feedback ID.
     * @param studentID the student identification of the evidence.
     * @return integer with the feedback ID.
     * @throws SQLException if there was an error with the database.
     */
    @Override
    public int getFeedbackIDByEvidenceID(int evidenceID, String studentID) throws SQLException {
        int result = 0;
        String query = "SELECT ID_retroalimentacion AS id FROM Retroalimentacion " +
                "INNER JOIN Evidencias ON Retroalimentacion.ID_evidencia = Evidencias.ID_evidencia " +
                "WHERE Retroalimentacion.ID_evidencia = (?) AND Evidencias.matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, evidenceID);
        preparedStatement.setString(2, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            result = resultSet.getInt("id");
        }

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param evidenceID the evidence identification for the number of feedbacks.
     * @param studentID the student identification of the evidence.
     * @return integer with the number of feedbacks.
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public int getFeedbacksByEvidenceID(int evidenceID, String studentID) throws SQLException {
        int result = 0;
        String query = "SELECT COUNT(retroalimentacion.ID_evidencia) AS evidencias FROM Retroalimentacion " +
                "INNER JOIN Evidencias ON Retroalimentacion.ID_evidencia = Evidencias.ID_evidencia " +
                "WHERE Retroalimentacion.ID_evidencia = (?) AND Evidencias.matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, evidenceID);
        preparedStatement.setString(2, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            result = resultSet.getInt("evidencias");
        }

        databaseManager.closeConnection();
        return result;
    }

    /**
     * @param evidenceID the evidence identification for the feedback text.
     * @param studentID the student identification of the evidence.
     * @return feedback text.
     * @throws SQLException if there was a problem connecting to the database or getting the data from a column.
     */
    @Override
    public String  getFeedbackTextByEvidenceID(int evidenceID, String studentID) throws SQLException {
        String  result = null;
        String query = "SELECT textoRetroalimentacion AS texto FROM Retroalimentacion " +
                "INNER JOIN Evidencias ON Retroalimentacion.ID_evidencia = Evidencias.ID_evidencia " +
                "WHERE Retroalimentacion.ID_evidencia = (?) AND Evidencias.matriculaEstudiante = (?)";
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, evidenceID);
        preparedStatement.setString(2, studentID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            result = resultSet.getString("texto");
        }

        databaseManager.closeConnection();
        return result;
    }

}
