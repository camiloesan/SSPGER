package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.gui.LoginController;
import mx.uv.fei.logic.AccessAccount;
import mx.uv.fei.logic.Professor;
import mx.uv.fei.logic.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    @BeforeEach
    void setUp() throws SQLException {
        var userDAO = new UserDAO();
        var accessAccount = new AccessAccount();
        var professor = new Professor();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("Profesor");
        professor.setProfessorName("albieri");
        professor.setProfessorLastName("sanchez");
        professor.setProfessorEmail("hola@gmail.com");
        professor.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(accessAccount, professor);
        var accessAccount2 = new AccessAccount();
        var student = new Student();
        accessAccount2.setUsername("dummy2");
        accessAccount2.setUserPassword("dummy2");
        accessAccount2.setUserType("Estudiante");
        student.setStudentID("s21022342");
        student.setName("luis");
        student.setLastName("cardone");
        student.setAcademicEmail("luis@gmail.com");
        userDAO.addStudentUserTransaction(accessAccount2, student);
        var accessAccount3 =  new AccessAccount();
        accessAccount3.setUsername("adminDummy");
        accessAccount3.setUserPassword("admin");
        accessAccount3.setUserType("Administrador");
        userDAO.addAdminUser(accessAccount3);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var accessAccountDAO = new UserDAO();
        accessAccountDAO.deleteUserByUsername("dummy");
        accessAccountDAO.deleteUserByUsername("dummy2");
        accessAccountDAO.deleteUserByUsername("adminDummy");
    }

    @Test
    void testAddAdminUserAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        assertEquals(0, userDAO.addAdminUser(accessAccount));
    }

    @Test
    void testAddAdminUserUsernameTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("eFAJBmZuSjbFigJU8pjhySP8MYZg2f");
        accessAccount.setUserPassword("dummy");
        assertEquals(0, userDAO.addAdminUser(accessAccount));
    }

    @Test
    void testAddStudentUserTransactionUserAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        student.setStudentID("abcdef");
        student.setName("dummy");
        student.setLastName("dummy");
        student.setAcademicEmail("dummy@gmail.com");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionIDTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("dummyx");
        accessAccount.setUserPassword("dummy");
        student.setStudentID("12345678901");
        student.setName("dummy");
        student.setLastName("dummy");
        student.setAcademicEmail("dummy@gmail.com");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionLastNameTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("dummyx");
        accessAccount.setUserPassword("dummy");
        student.setStudentID("1234567890");
        student.setName("dummy");
        student.setLastName("uxyX7JL9tSGj3kRYuIQk1fMWjJCX9S2lLIzF9EtTLRyyns3o6o6QOqVHFXOsFEHRVR25RSTVAOEj1V43x");
        student.setAcademicEmail("dummy@gmail.com");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionEmailTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("dummyx");
        accessAccount.setUserPassword("dummy");
        student.setStudentID("1234567890");
        student.setName("dummy");
        student.setLastName("dummy");
        student.setAcademicEmail("uxyX7JL9tSGj3kRYuIQk1fMWjJCX9S2lLIzF9EtTLRyyns3o6o6QOqVHFXOsFEHRVR25RSTVAOEj1V43x");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddProfessorUserTransactionUserAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Professor professor = new Professor();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        professor.setProfessorName("dummy");
        professor.setProfessorLastName("dummy");
        professor.setProfessorEmail("dummy@gmail.com");
        assertFalse(userDAO.addProfessorUserTransaction(accessAccount, professor));
    }

    @Test
    void testModifyStudentUserTransactionIncorrectUserType() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("new");
        accessAccount.setUserPassword("new");
        accessAccount.setUserType(LoginController.USER_ADMIN);
        student.setStudentID("1234567890");
        student.setName("new");
        student.setLastName("new");
        student.setAcademicEmail("x@gmail.com");
        assertFalse(userDAO.modifyStudentUserTransaction("dummy", accessAccount, student));
    }

    @Test
    void testModifyStudentUserTransactionUserAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy2");
        accessAccount.setUserType(LoginController.USER_ADMIN);
        student.setStudentID("1234567890");
        student.setName("new");
        student.setLastName("new");
        student.setAcademicEmail("x@gmail.com");
        assertFalse(userDAO.modifyStudentUserTransaction("dummy", accessAccount, student));
    }

    @Test
    void testModifyProfessorUserTransactionIncorrectUserType() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Professor professor = new Professor();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("nag");
        accessAccount.setUserType(LoginController.USER_ADMIN);
        professor.setProfessorName("pepe");
        professor.setProfessorLastName("gonzales");
        professor.setProfessorDegree("Dr.");
        professor.setProfessorEmail("some@gmail.com");
        assertFalse(userDAO.modifyProfessorUserTransaction("dummy", accessAccount, professor));
    }

    @Test
    void testModifyProfessorUserTransactionIncorrectDegree() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Professor professor = new Professor();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy2");
        accessAccount.setUserType(LoginController.USER_ADMIN);
        professor.setProfessorName("pepe");
        professor.setProfessorLastName("gonzales");
        professor.setProfessorDegree("incorrect");
        professor.setProfessorEmail("some@gmail.com");
        assertFalse(userDAO.modifyProfessorUserTransaction("dummy", accessAccount, professor));
    }

    @Test
    void testDeleteUserByUsernameShouldDelete() throws SQLException {
        UserDAO userDAO = new UserDAO();
        assertEquals(1, userDAO.deleteUserByUsername("dummy"));
    }

    @Test
    void testAreCredentialsValidTrue() throws SQLException {
        var userDAO = new UserDAO();
        assertTrue(userDAO.areCredentialsValid("dummy", "dummy"));
    }

    @Test
    void testAreCredentialsValidFalse() throws SQLException {
        UserDAO userDAO = new UserDAO();
        assertFalse(userDAO.areCredentialsValid("dummyn", "camilo"));
    }

    @Test
    void testGetAccessAccountTypeByUsernameShouldReturnProfessor() throws SQLException {
        UserDAO userDAO = new UserDAO();
        assertEquals("Profesor", userDAO.getAccessAccountTypeByUsername("dummy"));
    }

    @Test
    void testGetAccessAccountTypeByUsernameShouldReturnStudent() throws SQLException {
        UserDAO userDAO = new UserDAO();
        assertEquals("Estudiante", userDAO.getAccessAccountTypeByUsername("dummy2"));
    }
}