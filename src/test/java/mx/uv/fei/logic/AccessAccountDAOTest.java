package mx.uv.fei.logic;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AccessAccountDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount();
        accessAccount.setUsername("dummy");
        accessAccount.setUserPassword("dummy");
        accessAccount.setUserType("profesor");
        accessAccountDAO.addAccessAccount(accessAccount);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var accessAccountDAO = new AccessAccountDAO();
        accessAccountDAO.deleteAccessAccountByUsername("dummy");
    }

    @Test
    void testAddAccessAccountWrongUserType() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("dummy", "dummy", "coche");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAccessAccount(accessAccount));
    }

    @Test
    void addAccessAccountUserAlreadyExists() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("dummy", "dummy", "profesor");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAccessAccount(accessAccount));
    }

    @Test
    void addAccessAccountUsernameTooLong() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("1234567890123456", "12345", "administrador");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAccessAccount(accessAccount));
    }

    @Test
    void addAccessAccountPasswordTooLong() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("jose", "123456789012345678", "administrador");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAccessAccount(accessAccount));
    }

    @Test
    void modifyAccessAccountByUsernameAlreadyExists() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("dummy", "dummy", "profesor");
        assertThrows(SQLException.class, () -> accessAccountDAO.modifyAccessAccountByUsername("camilo", accessAccount));
    }

    @Test
    void modifyAccessAccountByUsernameTooLong() {

    }

    @Test
    void modifyAccessAccountByUsernamePasswordTooLong() {

    }

    @Test
    void modifyAccessAccountByUsernameWrongUserType() {

    }

    @Test
    void areCredentialsValid() {
        var accessAccountDAO = new AccessAccountDAO();
        assertDoesNotThrow(() -> accessAccountDAO.areCredentialsValid("dummy", "dummy"));
    }
}