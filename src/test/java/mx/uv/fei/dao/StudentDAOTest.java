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
        AccessAccount accessAccount = new AccessAccount();
        UserDAO accessAccountDAO = new UserDAO();

        accessAccount.setUsername("BDMG");
        accessAccount.setUserPassword("contrasenaBDMG");
        accessAccount.setUserType("Estudiante");

        accessAccountDAO.addAdminUser(accessAccount);
    }

    @AfterEach
    void tearDown() throws SQLException {
       UserDAO accessAccountDAO = new UserDAO();
        StudentDAO studentDAO = new StudentDAO();

        accessAccountDAO.deleteUserByUsername("BDMG");
        studentDAO.deleteStudent("ZS21013865");
    }

    @Test
    void testInsertStudentSucces() throws SQLException {
        Student student = new Student();
        StudentDAO studentDAO = new StudentDAO();

        student.setStudentID("ZS21013865");
        student.setName("Bryam Danae");
        student.setLastName("Morales García");
        student.setAcademicEmail("zs21013865@estudiantes.uv.mx");

        int expectedResult = 1;
        int result = studentDAO.insertStudent(student);
        assertEquals(expectedResult,result);
    }

    @Test
    void deleteStudent() {
    }
}