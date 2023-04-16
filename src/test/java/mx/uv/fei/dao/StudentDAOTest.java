package mx.uv.fei.dao;

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
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();

        accessAccount.setUsername("BDMG");
        accessAccount.setUserPassword("contrasenaBDMG");
        accessAccount.setUserType("Estudiante");

        accessAccountDAO.addAccessAccount(accessAccount);
    }

    @AfterEach
    void tearDown() throws SQLException {
       AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        StudentDAO studentDAO = new StudentDAO();

        accessAccountDAO.deleteAccessAccountByUsername("BDMG");
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
        student.setNRC(74293);
        student.setUserID(5);

        int expectedResult = 1;
        int result = studentDAO.insertStudent(student);
        assertEquals(expectedResult,result);
    }

    @Test
    void deleteStudent() {
    }
}