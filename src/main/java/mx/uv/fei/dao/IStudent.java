package mx.uv.fei.dao;

import mx.uv.fei.logic.Student;

import java.sql.SQLException;
import java.util.List;

public interface IStudent {
    int insertStudent(Student student) throws SQLException;
    int deleteStudent(String studentID) throws  SQLException;
}
