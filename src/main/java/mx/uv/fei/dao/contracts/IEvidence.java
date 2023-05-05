package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;
import java.util.List;

public interface IEvidence {
    int addEvidence(Evidence evidence) throws SQLException;
    void updateEvidenceGradeById(String id, int grade) throws SQLException;
    int modifyEvidence(int evidenceID, String evidenceTitle, String evidenceDescription) throws SQLException;
    List<Evidence> getEvidenceListByStudentId(String id) throws SQLException;

    Evidence getEvidenceByEvidenceTitle(String id) throws SQLException;

    int getAdvancementIDByEvidenceTitle(String evidenceTitle) throws SQLException;

    String getStudentIDByEvidenceTitle(String evidenceTitle) throws SQLException;

    int deleteEvidenceByName(String evidenceName) throws SQLException;

    List<Evidence> getEvidenceListByStudent(String studentID) throws SQLException;
}
