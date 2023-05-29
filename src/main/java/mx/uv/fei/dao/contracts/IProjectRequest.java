package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.ProjectRequest;

import java.sql.SQLException;
import java.util.List;

public interface IProjectRequest {
    int createProjectRequest(ProjectRequest projectPetition) throws SQLException;
    int validateProjectRequest(String validation, int projectPetitionID) throws SQLException;
    int deleteProjectRequest(int projectPetitionID) throws  SQLException;
    List<ProjectRequest> getProjectRequestsListByProfessorId(int professorId) throws SQLException;
    int getProjectRequestsByStudentID(String studentID) throws SQLException;
    int getProjectRequestIDByStudentID(String studentID) throws SQLException;
    ProjectRequest getProjectRequestInfoByStudentID(String studentID) throws SQLException;
}
