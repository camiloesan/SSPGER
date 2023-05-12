package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IProject {
    int addProject(Project project) throws SQLException;
    int setDirectorIDtoProject(Project projectDirectorName) throws SQLException;
    int setCodirectorIDtoProject(Project projectCodirectorName) throws SQLException;
    int updateProjectState(int projectId, String state) throws SQLException;
    int getProjectIDByTitle(String title) throws SQLException;
    ArrayList<DetailedProject> getProjectsByState(String projectState) throws SQLException;
    List<DetailedProject> getAllProjects() throws SQLException; //similar to getProjectsByState but to get all projects no matter the status of the project
    List<DetailedProject> getProjectsByRole(int professorID) throws SQLException;
    DetailedProject getProjectInfoByID(int projectID) throws SQLException;
    List<String> getLgacList() throws SQLException;
    List<String> getRWModalitiesList() throws SQLException;
    List<String> getAcademicBodyIDs() throws  SQLException;
    List<String> getProjectNamesByIdDirector(int directorId) throws SQLException;
    String getProjectNameById(int projectId) throws SQLException;
    int deleteProjectByTitle(String title) throws SQLException;
}
