package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IProject {
    int addProject(Project project) throws SQLException;
    int updateProjectState(int projectId, String state) throws SQLException;
    ArrayList<DetailedProject> getProjectsByState(String projectState) throws SQLException;
    DetailedProject getProjectInfo(String projectTitle) throws SQLException;
    List<String> getLgacList() throws SQLException;
    List<String> getRWModalitiesList() throws SQLException;
    List<String> getAcademicBodyIDs() throws  SQLException;
    List<Integer> getNRCs() throws SQLException;
    List<String> getProjectNamesByIdDirector(int directorId) throws SQLException;
}
