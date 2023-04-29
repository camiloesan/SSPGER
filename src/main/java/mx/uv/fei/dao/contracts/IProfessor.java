package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Professor;

import java.sql.SQLException;
import java.util.List;

public interface IProfessor {
    int addProfessor(Professor professor) throws SQLException;
    int updateProfessorByName(Professor updatedProfessor, String professorNameToUpdate) throws SQLException;
    List<Professor> getAllProfessors() throws SQLException;
    int deleteProfessorByName(String professorNameToDelete) throws SQLException;
    List<String> getProfessorsNames() throws SQLException;
}
