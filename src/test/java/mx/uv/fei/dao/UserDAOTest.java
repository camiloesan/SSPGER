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
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("profesor");
        userDAO.addAdminUser(accessAccount);
        var accessAccount2 = new AccessAccount();
        accessAccount2.setUsername("dummy2");
        accessAccount2.setUserPassword("dummy2");
        accessAccount2.setUserType("profesor");
        userDAO.addAdminUser(accessAccount2);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var accessAccountDAO = new UserDAO();
        accessAccountDAO.deleteUserByUsername("dummy");
        accessAccountDAO.deleteUserByUsername("dummy2");
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

    @Test //addProfessor
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
    void testModifyProfessorUserTransaction() {

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
}