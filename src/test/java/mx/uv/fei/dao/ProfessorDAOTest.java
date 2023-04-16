package mx.uv.fei.dao;

import mx.uv.fei.logic.Professor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();
        AccessAccount accessAccount = new AccessAccount();

        accessAccount.setUsername("ocharanJorgeO");
        accessAccount.setUserPassword("contrasenaJOOH");

        accessAccountDAO.addAccessAccount(accessAccount);
    }

    @AfterEach
    void tearDown() throws SQLException {
        AccessAccountDAO accessAccountDAO = new AccessAccountDAO();

        accessAccountDAO.deleteAccessAccountByName("ocharanJorgeO");
    }

    @Test
    void testAddProfessorSuccess() throws SQLException {
        System.out.println("addProfesor");
        Professor professor = new Professor();

        professor.setProfessorName("Jorge Octavio");
        professor.setProfessorFirsLastName("Ocharan");
        professor.setProfessorSecondLastName("Hernandez");
        professor.setProfessorEmail("jocharan@uv.mx");
        professor.setUserId(4);

        ProfessorDAO instance = new ProfessorDAO();
        int expectedResult = 1;
        int result = instance.addProfessor(professor);
        assertEquals(expectedResult,result);
    }
}
