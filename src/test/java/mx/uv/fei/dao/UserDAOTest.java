package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.UserDAO;
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
        accessAccount.setUsername("juaperez");
        accessAccount.setUserPassword("contrasenia2020");
        accessAccount.setUserEmail("juaperez@uv.mx");
        accessAccount.setUserType("Profesor");
        professor.setProfessorName("Juan Carlos");
        professor.setProfessorLastName("Pérez Arriaga");
        professor.setProfessorDegree("MCC.");
        userDAO.addProfessorUserTransaction(accessAccount, professor);
        var accessAccount2 = new AccessAccount();
        var student = new Student();
        accessAccount2.setUsername("zs21013861");
        accessAccount2.setUserPassword("contrasenia2020");
        accessAccount2.setUserEmail("zs21013861@estudiantes.uv.mx");
        accessAccount2.setUserType("Estudiante");
        student.setStudentID("s21013861");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        userDAO.addStudentUserTransaction(accessAccount2, student);
        var accessAccount3 =  new AccessAccount();
        accessAccount3.setUsername("administrador");
        accessAccount3.setUserPassword("administrador");
        accessAccount3.setUserEmail("administradorSSPGER@uv.mx");
        accessAccount3.setUserType("Administrador");
        userDAO.addAdminUser(accessAccount3);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var accessAccountDAO = new UserDAO();
        accessAccountDAO.deleteUserByUsername("juaperez");
        accessAccountDAO.deleteUserByUsername("zs21013861");
        accessAccountDAO.deleteUserByUsername("administrador");
    }

    @Test
    void testAddAdminUserSuccess() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("JoséMadero");
        accessAccount.setUserEmail("jose@uv.mx");
        accessAccount.setUserPassword("administrador");
        assertEquals(1, userDAO.addAdminUser(accessAccount));
        userDAO.deleteUserByUsername("JoséMadero");
    }

    @Test
    void testAddAdminUserAlreadyExists() {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("administrador");
        accessAccount.setUserEmail("administrador@uv.mx");
        accessAccount.setUserPassword("administrador");
        assertThrows(SQLException.class, () -> userDAO.addAdminUser(accessAccount));
    }

    @Test
    void testAddAdminUserEmailTooLong() {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("JoséMadero");
        accessAccount.setUserEmail("JoséMaderoVizcaínoPerezRodríguezGonzáles@uv.mx");
        accessAccount.setUserPassword("administrador");
        assertThrows(SQLException.class, () -> userDAO.addAdminUser(accessAccount));
    }

    @Test
    void testAddAdminUserUsernameTooLong() {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        accessAccount.setUsername("jorge roberto martinez perez fernandez juarez Giménez");
        accessAccount.setUserPassword("jorgeRoberto");
        accessAccount.setUserEmail("jorge@estudiantes.uv.mx");
        assertThrows(SQLException.class, () -> userDAO.addAdminUser(accessAccount));
    }

    @Test
    void testAddStudentUserTransactionUserAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013861");
        accessAccount.setUserPassword("oldHouse");
        accessAccount.setUserEmail("zs21013862@Estudiantes.uv.mx");
        student.setStudentID("s21013862");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionUserSuccess() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013863");
        accessAccount.setUserPassword("oldHouse");
        accessAccount.setUserEmail("zs21013863@estudiantes.uv.mx");
        student.setStudentID("s21013863");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertTrue(userDAO.addStudentUserTransaction(accessAccount, student));
        userDAO.deleteUserByUsername("zs21013863");
    }

    @Test
    void testAddStudentUserTransactionUserEmailAlreadyExists() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013863");
        accessAccount.setUserPassword("oldHouse");
        accessAccount.setUserEmail("zs21013861@estudiantes.uv.mx");
        student.setStudentID("s21013862");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionIDTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013862");
        accessAccount.setUserPassword("zs21012020");
        accessAccount.setUserEmail("zs21013860@estudiantes.uv.mx");
        student.setStudentID("zs210138611");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionLastNameTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013862");
        accessAccount.setUserPassword("zs21022020");
        accessAccount.setUserEmail("zs21022020@estudiantes.uv.mx");
        student.setStudentID("1234567890");
        student.setName("Jorge");
        student.setLastName("José Madero Vizcaíno Pérez Solano Roberto Tercero Peep Juarez Hernandeez Macip Ramírez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionNameTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013862");
        accessAccount.setUserPassword("zs21022020");
        accessAccount.setUserEmail("zs21022020@estudiantes.uv.mx");
        student.setStudentID("1234567890");
        student.setName("José Madero Vizcaíno Pérez Solano Roberto Tercero Peep Juarez Hernandez Macip Ramírez");
        student.setLastName("Giménez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testAddStudentUserTransactionEmailTooLong() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("zs21013922");
        accessAccount.setUserEmail("zs2102202012311@estudiantes.uv.mx");
        accessAccount.setUserPassword("zs21803password");
        student.setStudentID("zs21013862");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertFalse(userDAO.addStudentUserTransaction(accessAccount, student));
    }

    @Test
    void testModifyStudentUserTransactionIncorrectUserType() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Student student = new Student();
        accessAccount.setUsername("new");
        accessAccount.setUserPassword("new");
        accessAccount.setUserEmail("zs21013860@estudiantes.uv.mx");
        accessAccount.setUserType("Administrador");
        student.setStudentID("s21013890");
        student.setName("Camilo");
        student.setLastName("Espejo Sánchez");
        assertFalse(userDAO.modifyStudentUserTransaction("juaperez", accessAccount, student));
    }

    @Test
    void testModifyProfessorUserTransactionIncorrectUserType() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Professor professor = new Professor();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("nag");
        accessAccount.setUserType("Administrador");
        professor.setProfessorName("pepe");
        professor.setProfessorLastName("gonzales");
        professor.setProfessorDegree("Dr.");
        assertFalse(userDAO.modifyProfessorUserTransaction("dummy", accessAccount, professor));
    }

    @Test
    void testModifyProfessorUserTransactionIncorrectDegree() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccessAccount accessAccount = new AccessAccount();
        Professor professor = new Professor();
        accessAccount.setUsername("robertoGonzales");
        accessAccount.setUserPassword("roberto2023");
        accessAccount.setUserType("Profesor");
        professor.setProfessorName("José Roberto");
        professor.setProfessorLastName("González");
        professor.setProfessorDegree("");
        assertFalse(userDAO.modifyProfessorUserTransaction("zs21013861", accessAccount, professor));
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