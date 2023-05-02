package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.logic.DetailedProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProject() {
    }

    @Test
    void testGetProjectsByState() throws SQLException {
        System.out.println("Test getProjectsByState");
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.getProjectsByState("Por revisar");

        ArrayList<DetailedProject> expectedResult = new ArrayList<>();
        ArrayList<DetailedProject> result = new ArrayList<>(projectDAO.getProjectsByState("Verificado"));
        assertEquals(expectedResult, result);
    }

    @Test
    void testUpdateProjectState() throws SQLException {
        System.out.println("Test updateProjectState");
    }
}