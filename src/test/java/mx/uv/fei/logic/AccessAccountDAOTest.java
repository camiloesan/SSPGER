package mx.uv.fei.logic;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AccessAccountDAOTest {
    @Test
    void addAccessAccountShouldNotAdd() {
        var accessAccountDAO = new AccessAccountDAO();
        var accessAccount = new AccessAccount("jorge", "pass", "coche");
        assertThrows(SQLException.class, () -> {
            accessAccountDAO.addAccessAccount(accessAccount);
        });
    }

    @Test
    void modifyAccessAccountByUsername() {

    }

    @Test
    void deleteAccessAccountByName() {
    }

    @Test
    void areCredentialsValid() {
    }
}