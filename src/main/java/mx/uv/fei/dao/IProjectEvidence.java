package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectEvidence;

import java.sql.SQLException;
import java.util.List;

public interface IProjectEvidence {
    List<ProjectEvidence> getProjectEvidence() throws SQLException;
}
