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

        student.setStudentID("example");
        student.setName("example");
        student.setLastName("example");
        student.setUsername("example");

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
    void testGetNameByStudentIDSuccess() throws SQLException {
        var studentDAO = new StudentDAO();
        String expectedResult = "example";
        String result = studentDAO.getNamebyStudentID("example");
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetNameByStudentIDNotSuccess() throws SQLException {
        var studentDAO = new StudentDAO();
        String expectedResult = "fail";
        String result = studentDAO.getNamebyStudentID("example");
        assertNotEquals(expectedResult, result);
    }

    @Test
    void testGetStudentIDByUsernameSuccess() throws SQLException {
        var studentDAO = new StudentDAO();
        String expectedResult = "example";
        String result = studentDAO.getStudentIdByUsername("example");
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetStudentIDByUsernameNotSuccess() throws SQLException {
        var studentDAO = new StudentDAO();
        String expectedResult = "fail";
        String result = studentDAO.getStudentIdByUsername("example");
        assertNotEquals(expectedResult, result);
    }


}