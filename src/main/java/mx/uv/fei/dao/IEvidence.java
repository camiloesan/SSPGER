package mx.uv.fei.dao;

import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;
import java.util.List;

public interface IEvidence {
    void addEvidence(Evidence evidence) throws SQLException;
    void updateEvidenceGradeById(String id, int grade) throws SQLException;
    List<Evidence> getEvidenceListByStudentId(String id) throws SQLException;
    int deleteEvidenceByName(String evidenceName) throws SQLException;
}
