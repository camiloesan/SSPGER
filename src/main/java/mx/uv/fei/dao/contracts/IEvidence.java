package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;
import java.util.List;

public interface IEvidence {
    int addEvidence(Evidence evidence) throws SQLException;
    int updateEvidenceGradeById(int id, int grade) throws SQLException;
    int modifyEvidence(Evidence evidence) throws SQLException;
    List<Evidence> getEvidenceListByProfessorID(int professorID) throws SQLException;

    Evidence getEvidenceByEvidenceTitle(String id) throws SQLException;

    int getAdvancementIDByEvidenceTitle(String evidenceTitle) throws SQLException;

    String getStudentIDByEvidenceTitle(String evidenceTitle) throws SQLException;

    int deleteEvidenceByID(int evidenceID) throws SQLException;

    List<Evidence> getEvidenceListByStudent(String studentID) throws SQLException;

    String getAdvancementNameByStudentID(String studentID, int advancementID) throws SQLException;
    List<Evidence> getDeliveredEvidences(String studentID) throws SQLException;
}
