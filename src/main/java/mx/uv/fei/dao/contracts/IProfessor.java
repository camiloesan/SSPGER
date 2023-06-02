package mx.uv.fei.dao.contracts;


import java.sql.SQLException;
import java.util.List;

public interface IProfessor {
    List<String> getProfessorsNames() throws SQLException;
    int getProfessorIdByUsername(String username) throws SQLException;
    String getDirectorsByProject(int projectID) throws SQLException;
}
