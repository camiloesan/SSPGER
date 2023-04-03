package mx.uv.fei.dao;

import java.sql.SQLException;

public interface IProjectEvidence {
    void addProjectEvidence() throws SQLException;
    void addProjectEvidenceGrade() throws SQLException;
    void getProjectEvidenceByStudent() throws SQLException;
}
