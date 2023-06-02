package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Professor;

import java.sql.SQLException;
import java.util.List;

public interface IProfessor {
    int addProfessor(Professor professor) throws SQLException;
    List<String> getProfessorsNames() throws SQLException;
    int getProfessorIdByUsername(String username) throws SQLException;
    String getDirectorsByProject(int projectID) throws SQLException;
}
