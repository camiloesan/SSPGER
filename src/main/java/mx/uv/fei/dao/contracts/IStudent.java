package mx.uv.fei.dao.contracts;

import mx.uv.fei.logic.Student;

import java.sql.SQLException;
import java.util.List;

public interface IStudent {
    int insertStudent(Student student) throws SQLException;
    int  deleteStudent(String studentID) throws  SQLException;
    String getNamebyStudentID(String studentID) throws SQLException;
    String getStudentIdByUsername(String username) throws SQLException;
    //String getStudentIDByProfessorID(int professorID) throws SQLException;
    List<Student> getStudentsByProjectID(int projectID) throws SQLException;
    boolean isStudentIDTaken(String studentID) throws SQLException;
}
