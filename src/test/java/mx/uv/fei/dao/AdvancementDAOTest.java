package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.logic.Advancement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AdvancementDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddAdvancementSuces() throws SQLException {
        Advancement advancement = new Advancement();
        AdvancementDAO advancementDAO = new AdvancementDAO();

        advancement.setAdvancementName("Avance1");
        advancement.setAdvancementDescription("descripcion");
        advancement.setAdvancementStartDate("2023-04-16");
        advancement.setAdvancementDeadline("2023-05-16");
        advancement.setProfessorId(1);
        advancement.setProjectId(1);
        int expectedResult = 1;
        int result = advancementDAO.addAdvancement(advancement);
        assertEquals(expectedResult,result);
    }
}