package mx.uv.fei.dao;

import mx.uv.fei.logic.DetailedProject;
import mx.uv.fei.logic.Project;
import mx.uv.fei.logic.SimpleProject;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IProject {
    int addProject(Project project) throws SQLException;
    int updateProjectState(int projectId, String state) throws SQLException;
    ArrayList<SimpleProject> getProjectsByState(String projectState) throws SQLException;
    DetailedProject getProjectInfo(int projectID) throws SQLException;
}
