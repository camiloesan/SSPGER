package mx.uv.fei.dao;

import mx.uv.fei.logic.Project;

import java.sql.SQLException;

public interface IProject {
    int addProject(Project project) throws SQLException;
}
