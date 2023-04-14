package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectPetitions;

import java.sql.SQLException;
import java.util.List;

public interface IProjectPetitions {
    void createProjectPetition(ProjectPetitions projectPetition) throws SQLException;
    void validateProjectPetition(String validation, int projectPetitionID) throws SQLException;
    void deleteProjectPetition(int projectPetitionID) throws  SQLException;
}
