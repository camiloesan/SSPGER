package mx.uv.fei.dao;

import mx.uv.fei.logic.Evidence;

import java.sql.SQLException;

public interface IEvidence {
    void addProjectEvidence(Evidence evidence) throws SQLException;
    void updateProjectEvidenceGradeById(String id, int grade) throws SQLException;
    void getProjectEvidenceByStudentId(String id) throws SQLException;
}
