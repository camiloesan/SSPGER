package mx.uv.fei.dao;

import mx.uv.fei.logic.Professor;

import java.sql.SQLException;

public interface IProfessor {
    int addProfessor(Professor professor) throws SQLException;
}
