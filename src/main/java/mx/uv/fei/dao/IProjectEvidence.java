package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectEvidence;

import java.sql.SQLException;

public interface IProjectEvidence {
    void addProjectEvidence(ProjectEvidence projectEvidence) throws SQLException;
    void updateProjectEvidenceGradeById(String id, int grade) throws SQLException;
    void getProjectEvidenceByStudentId(String id) throws SQLException;
}
