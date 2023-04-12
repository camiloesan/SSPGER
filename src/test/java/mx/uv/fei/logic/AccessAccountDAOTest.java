package mx.uv.fei.logic;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AccessAccountDAOTest {
    @Test
    void addAccessAccountWrongUserType() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("jorge", "pass", "coche");
        assertThrows(SQLException.class, () -> accessAccountDAO.addAccessAccount(accessAccount));
    }

    @Test
    void addAccessAccountUserAlreadyExists() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("camilo", "camilo", "administrador");
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
        var accessAccount = new AccessAccount("camilo", "camilo", "administrador");
        assertThrows(SQLException.class, () -> accessAccountDAO.modifyAccessAccountByUsername("jorge", accessAccount));
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
    void deleteAccessAccountByUsername() {
        var accessAccountDAO = new AccessAccountDAO();
        assertFalse(false);
    }

    @Test
    void areCredentialsValid() {

    }
}