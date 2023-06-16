package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.ProjectRequest;

import java.sql.SQLException;
import java.util.List;

public interface IProjectRequest {
    int createProjectRequest(ProjectRequest projectRequest) throws SQLException;
    int validateProjectRequest(String validation, int projectRequestID) throws SQLException;
    int deleteProjectRequest(int projectRequestID) throws  SQLException;
    List<ProjectRequest> getProjectRequestsListByProfessorId(int professorId) throws SQLException;
    int getProjectRequestsByStudentID(String studentID) throws SQLException;
    int getProjectRequestIDByStudentID(String studentID) throws SQLException;
    ProjectRequest getProjectRequestInfoByStudentID(String studentID) throws SQLException;
    boolean isRequestApproved(String studentID) throws SQLException;
}
