package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Feedback;

import java.sql.SQLException;

public interface IFeedback {
    int addFeedback(Feedback feedback) throws SQLException;
    int deleteFeedbackByID(int feedbackID) throws SQLException;
    int getFeedbackIDByEvidenceID(int evidenceID, String studentID) throws SQLException;
    int getFeedbacksByEvidenceID(int evidenceID, String studentID) throws SQLException;
    String  getFeedbackTextByEvidenceID(int evidenceID, String studentID) throws SQLException;
}
