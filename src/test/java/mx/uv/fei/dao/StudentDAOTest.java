package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        Student student = new Student();
        StudentDAO studentDAO = new StudentDAO();

        student.setStudentID("example");
        student.setName("example");
        student.setLastName("example");
        student.setAcademicEmail("example");
        student.setUsername("example");

        studentDAO.insertStudent(student);

        AccessAccount accessAccount = new AccessAccount();
        UserDAO accessAccountDAO = new UserDAO();

        accessAccount.setUsername("example");
        accessAccount.setUserPassword("example");
        accessAccount.setUserType("Estudiante");

        accessAccountDAO.addStudentUserTransaction(accessAccount, student);
    }

    @AfterEach
    void tearDown() throws SQLException {
       UserDAO accessAccountDAO = new UserDAO();
       StudentDAO studentDAO = new StudentDAO();

       studentDAO.deleteStudent("example");
       accessAccountDAO.deleteUserByUsername("example");
    }

    @Test
    void testInsertStudentSuccess() throws SQLException {
        var student = new Student();
        var accessAccount = new AccessAccount();
        var userDAO = new UserDAO();
        var studentDAO = new StudentDAO();

        accessAccount.setUsername("exampleToInsert");
        accessAccount.setUserPassword("example");
        accessAccount.setUserType("Estudiante");

        student.setStudentID("example");
        student.setName("example");
        student.setLastName("example");
        student.setAcademicEmail("example");
        student.setUsername("exampleToInsert");

        userDAO.addStudentUserTransaction(accessAccount, student);

        int expectedResult = 1;
        int result = studentDAO.insertStudent(student);
        assertEquals(expectedResult, result);
    }

    @Test
    void deleteStudent() {

    }
}