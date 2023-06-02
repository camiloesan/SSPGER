package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.AdvancementDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class
AdvancementDAOTest {
    Professor professor = new Professor();
    Project project = new Project();
    Advancement advancement = new Advancement();
    Student student = new Student();

    @BeforeEach
    void setUp() throws SQLException {
        UserDAO userDAO = new UserDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();

        AdvancementDAO advancementDAO = new AdvancementDAO();
        AccessAccount accessAccountStudent = new AccessAccount();
        AccessAccount accessAccountProfessor = new AccessAccount();

        accessAccountProfessor.setUsername("testProf");
        accessAccountProfessor.setUserType("Profesor");
        accessAccountProfessor.setUserPassword("Profesor");
        professor.setProfessorName("testProfessor");
        professor.setProfessorLastName("testProfessor");
        professor.setProfessorEmail("testProfessor@mail.com");
        professor.setProfessorDegree("Dr.");

        accessAccountStudent.setUsername("testStudent");
        accessAccountStudent.setUserType("Estudiante");
        accessAccountStudent.setUserPassword("testStudent");
        student.setName("testStudent");
        student.setLastName("testStudent");
        student.setAcademicEmail("testStudent@gmail.com");
        student.setStudentID("zsTest");

        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName("Ejemplo proyecto investigación");
        project.setLGAC_Id(1);
        project.setInvestigationLine("Ejemplo linea investigación");
        project.setApproximateDuration("12 meses");
        project.setModalityId(1);
        project.setReceptionWorkName("Ejemplo trabajo recepcional");
        project.setRequisites("Ejemplo requisitos");
        project.setDirectorName("testProfessor");
        project.setCodirectorName("Ejemplo codirector");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("Ejemplo descripción de investigación");
        project.setReceptionWorkDescription("Ejemplo descripción trabajo recepcional");
        project.setExpectedResults("Ejemplo resultados esperados");
        project.setRecommendedBibliography("Ejemplo bibliografía");
        userDAO.addStudentUserTransaction(accessAccountStudent, student);
        userDAO.addProfessorUserTransaction(accessAccountProfessor, professor);
        projectDAO.addProject(project);

        advancement.setAdvancementName("testAdvancement");
        advancement.setAdvancementDescription("Description");
        advancement.setAdvancementStartDate("2023-05-29");
        advancement.setAdvancementDeadline("2023-06-27");
        advancement.setProjectId(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));
        advancementDAO.addAdvancement(advancement);

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));
        projectRequest.setStudentId("zsTest");
        projectRequest.setDescription("henlo");
        projectRequestDAO.createProjectRequest(projectRequest);
        projectRequestDAO.validateProjectRequest("Aceptado", projectRequestDAO.getProjectRequestIDByStudentID("zsTest"));
    }

    @AfterEach
    void tearDown() throws SQLException {
        UserDAO userDAO = new UserDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        projectRequestDAO.deleteProjectRequest(projectRequestDAO.getProjectRequestIDByStudentID("zsTest"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle(project.getReceptionWorkName()));
        advancementDAO.deleteAdvancementById(advancementDAO.getLastAdvancementID());
        userDAO.deleteUserByUsername("testProf");
        userDAO.deleteUserByUsername("testStudent");
    }

    @Test
    void testAddAdvancementWrongProjectId() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("zxb");
        advancement1.setAdvancementDescription("zxb");
        advancement1.setAdvancementStartDate("2023-05-29");
        advancement1.setAdvancementDeadline("2023-06-27");
        advancement1.setProjectId(0);
        assertThrows(SQLException.class, () -> advancementDAO.addAdvancement(advancement1));
    }

    @Test
    void testGetAdvancementDetailsByIdObject() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        Advancement expectedAdvancement = advancementDAO.getAdvancementDetailById(advancementDAO.getLastAdvancementID());
        assertEquals(advancement, expectedAdvancement);
    }

    @Test
    void testGetAdvancementListByProjectIdShouldBeCorrect() {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        int id = 0;
        assertDoesNotThrow(() -> advancementDAO.getAdvancementListByProjectId(id));
    }

    @Test
    void testModifyAdvancementByIdSuccess() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("new");
        advancement1.setAdvancementDescription("new");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));

        assertEquals(1, advancementDAO.modifyAdvancementById(advancementDAO.getLastAdvancementID(), advancement1));
    }

    @Test
    void testModifyAdvancementByIdWrongAdvancementId() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        Advancement advancement1 = new Advancement();
        advancement1.setAdvancementName("new");
        advancement1.setAdvancementDescription("new");
        advancement1.setAdvancementStartDate("2022-03-24");
        advancement1.setAdvancementDeadline("2024-02-03");
        advancement1.setProjectId(projectDAO.getProjectIDByTitle("Ejemplo trabajo recepcional"));

        assertEquals(0, advancementDAO.modifyAdvancementById(0, advancement1));
    }

    @Test
    void testDeleteAdvancementByIdShouldNotWrongAdvancementId() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        assertEquals(0, advancementDAO.deleteAdvancementById(0));
    }

    @Test
    void testGetProjectNameByStudentIDDidntFoundMatches() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        assertNull(advancementDAO.getProjectNameByStudentID("0x0"));
    }

    @Test
    void testGetProjectNameByStudentIDFoundElement() throws SQLException {
        AdvancementDAO advancementDAO = new AdvancementDAO();
        assertNotNull(advancementDAO.getProjectNameByStudentID("zsTest"));
    }
}