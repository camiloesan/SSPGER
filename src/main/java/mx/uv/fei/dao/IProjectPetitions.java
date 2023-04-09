package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectPetitions;

import java.sql.SQLException;
import java.util.List;

public interface IProjectPetitions {
    List<ProjectPetitions> getProjectPetitions() throws SQLException;
    void createProjectPetition(ProjectPetitions projectPetition) throws SQLException;
    void validateProjectPetition(String option, int projectID, String studentTuition) throws SQLException;
}
