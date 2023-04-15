package mx.uv.fei.dao;

import mx.uv.fei.logic.ProjectRequest;

import java.sql.SQLException;

public interface IProjectRequest {
    int createProjectPetition(ProjectRequest projectPetition) throws SQLException;
    int validateProjectPetition(String validation, int projectPetitionID) throws SQLException;
    int deleteProjectPetition(int projectPetitionID) throws  SQLException;
}
